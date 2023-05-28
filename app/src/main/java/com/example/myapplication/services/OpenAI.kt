package com.example.myapplication.services

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun openAI(prompt: String): String {
    val client = OkHttpClient()

    val jsonBody = """
        {
            "model": "text-davinci-003",
            "prompt": "$prompt",
            "max_tokens": 200,
            "temperature": 0
        }
    """.trimIndent()

    val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("https://api.openai.com/v1/completions")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer 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")
        .post(requestBody)
        .build()

    return try {
        val response = client.newCall(request).execute()

        val jsonObject = response.body?.string()?.let { JSONObject(it) }

        val result = jsonObject?.getJSONArray("choices")?.getJSONObject(0)?.get("text")

        result as String
    } catch (ex: Exception) {
        "Ocorreu um erro na consulta!"
    }

}