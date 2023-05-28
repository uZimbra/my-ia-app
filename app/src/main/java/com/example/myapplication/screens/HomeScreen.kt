package com.example.myapplication.screens

import android.content.ContentValues
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import com.example.myapplication.contracts.HistoryContract
import com.example.myapplication.contracts.NavigationContract
import com.example.myapplication.services.openAI
import java.util.concurrent.Executors

@Composable
fun HomeScreen(navController: NavController) {
    val ctx = LocalContext.current
    val userInputText = remember { mutableStateOf("") }
    val apiResponseText = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val buttonState = remember { mutableStateOf(true) }
    var showError by remember { mutableStateOf(false) }

    fun request(value: String) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            buttonState.value = false
            val result = openAI(value)
            apiResponseText.value = result
            userInputText.value = ""
            buttonState.value = true

            val values = ContentValues().apply {
                put(HistoryContract.COLUMN_NAME, value.trim())
                put(HistoryContract.COLUMN_VALUE, result.trim())
            }

            ctx.contentResolver.insert(HistoryContract.HISTORY_PROVIDER_URI, values)
        }
    }

    fun navigateToHistoryScreen() {
        navController.navigate(NavigationContract.HISTORY_SCREEN)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToHistoryScreen() }) {
                Icon(Icons.Filled.History, contentDescription = "History")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            Modifier
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            Column {
                Text(
                    text = "Digite algo para o assistente: ",
                    Modifier.padding(vertical = 10.dp)
                )

                BasicTextField(
                    value = userInputText.value,
                    onValueChange = { newValue ->
                        userInputText.value = newValue
                        showError = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFCF7FF), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    textStyle = TextStyle(color = Color.Black, fontSize = 5.em),
                )

                if (showError && userInputText.value.isEmpty()) {
                    Text(
                        text = "Preencha o campo",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    enabled = buttonState.value,
                    onClick = {
                        if (userInputText.value.isEmpty()) {
                            showError = true
                        } else {
                            request(userInputText.value)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(text = "Pesquisar")
                }

                if (apiResponseText.value.isNotEmpty()) {
                    SelectionContainer(Modifier.padding(10.dp)) {
                        Text(text = apiResponseText.value)
                    }
                }
            }
        }
    }

}
