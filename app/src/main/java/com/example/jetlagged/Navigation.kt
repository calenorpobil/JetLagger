package com.example.jetlagged

class Navigation {
    sealed class Screen(val route: String) {
        object Main : Screen("main")
        object Edit : Screen("edit")
    }

}