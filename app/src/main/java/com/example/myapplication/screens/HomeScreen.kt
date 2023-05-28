package com.example.myapplication.screens

import android.content.ContentValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.ApiResponseBox
import com.example.myapplication.components.UserInputText
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

                UserInputText(userInputText)

                Button(
                    enabled = buttonState.value,
                    onClick = { request(userInputText.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(text = "Pesquisar")
                }

                ApiResponseBox(apiResponseText.value)
            }
        }
    }

}
