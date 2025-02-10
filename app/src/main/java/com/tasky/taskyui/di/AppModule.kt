package com.tasky.taskyui.di

import com.tasky.taskyui.data.AuthenticationManager
import com.tasky.taskyui.ui.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AuthenticationManager() }
    viewModel { LoginViewModel() }
}
