package com.example.sinshangapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val text: String,
    val sender: String,
    val userId: String,
    val timestamp: String
)
