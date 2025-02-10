package com.tasky.taskyui.data

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val msg: String) : AuthResponse
}