package com.example.mint.models

data class Badge(
    val name: String,
    val description: String,
    val dateEarned: String,
    val iconResId: Int ,
    val isEarned: Boolean = true
)
