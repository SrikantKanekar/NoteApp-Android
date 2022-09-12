package com.example.note.util

import androidx.navigation.NavHostController

val NavHostController.route: String?
    get() = this.currentBackStackEntry?.destination?.route

// 1