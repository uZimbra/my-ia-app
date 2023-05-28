package com.example.myapplication.screens

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.contracts.HistoryContract
import com.example.myapplication.contracts.NavigationContract

@Composable
@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
fun HistoryScreen(navController: NavController) {
    val ctx = LocalContext.current
    val historyListState = remember { mutableStateOf(recoverCurrenciesFromDB(ctx.contentResolver)) }

    fun navigateToHomeScreen() {
        navController.navigate(NavigationContract.HOME_SCREEN)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Histórico",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToHomeScreen() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Arrow Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (historyListState.value.isNotEmpty()) {
                FloatingActionButton(onClick = { clearHistory(ctx, historyListState) }) {
                    Icon(Icons.Filled.DeleteForever, contentDescription = "DeleteForever")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        if (historyListState.value.isNotEmpty()) {
            LazyColumn(
                Modifier
                    .padding(top = 60.dp)
                    .fillMaxSize()
            ) {
                itemsIndexed(historyListState.value) { index, item ->
                    val (key, value) = item
                    Column(
                        Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text("Pergunta:", fontSize = 18.sp)
                        SelectionContainer {
                            Text(text = key)
                        }
                        Text("Resposta:", fontSize = 18.sp)
                        SelectionContainer {
                            Text(text = value)
                        }
                    }
                    if (index != historyListState.value.lastIndex) {
                        Divider()
                    }
                }
            }
        } else {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "O histórico esta vazio.")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun clearHistory(ctx: Context, historyListState: MutableState<List<Pair<String, String>>>) {
    ctx.contentResolver.delete(HistoryContract.HISTORY_PROVIDER_URI, null)
    historyListState.value = emptyList()
    Toast.makeText(ctx, "Histórico excluido com sucesso!", Toast.LENGTH_SHORT).show()
}

@SuppressLint("Range")
@RequiresApi(Build.VERSION_CODES.O)
fun recoverCurrenciesFromDB(contentResolver: ContentResolver): List<Pair<String, String>> {
    val projection = arrayOf(
        HistoryContract.COLUMN_NAME,
        HistoryContract.COLUMN_VALUE,
    )

    val cursor: Cursor? = contentResolver.query(
        HistoryContract.HISTORY_PROVIDER_URI,
        projection,
        null,
        null,
    )

    val list: MutableList<Pair<String, String>> = mutableListOf()

    if (cursor?.moveToFirst() == true) {
        while (!cursor.isAfterLast) {
            list.add(
                Pair(
                    cursor.getString(cursor.getColumnIndex(HistoryContract.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(HistoryContract.COLUMN_VALUE))
                )
            )
            cursor.moveToNext()
        }
    }

    cursor?.close()

    return list.toList()
}