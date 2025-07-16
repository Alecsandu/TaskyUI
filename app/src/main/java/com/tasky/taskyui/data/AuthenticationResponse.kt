package com.tasky.taskyui.data

sealed interface AuthenticationResponse {
    data object Success : AuthenticationResponse
    data class Error(val msg: String) : AuthenticationResponse
}