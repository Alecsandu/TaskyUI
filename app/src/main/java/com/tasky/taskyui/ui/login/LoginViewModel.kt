package com.tasky.taskyui.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasky.taskyui.data.AuthResponse
import com.tasky.taskyui.data.AuthenticationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    private val context: Context by inject()
    private val authManager: AuthenticationManager by inject()
    private val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        authManager.loginWithEmail(email, password)
            .onEach { response ->
                when (response) {
                    is AuthResponse.Success -> {
                        _loginState.value = LoginState.Success
                        with(sharedPrefs.edit()) {
                            putBoolean("is_logged_in", true)
                            apply()
                        }
                    }

                    is AuthResponse.Error -> _loginState.value = LoginState.Error(response.msg)
                }
            }
            .catch { e -> _loginState.value = LoginState.Error(e.message ?: "An error occurred") }
            .launchIn(viewModelScope)
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}

