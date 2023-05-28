package com.example.myapplication.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ApiResponseBox(text: String) {
    if (text.isNotEmpty()) {
        SelectionContainer(Modifier.padding(10.dp)) {
            Text(text = text)
        }
    }
}