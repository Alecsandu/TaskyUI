package com.tasky.taskyui.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tasky.taskyui.ui.theme.TaskyUITheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()
    LoginScreenContent(
        viewModel::login,
        viewModel::createAccount,
        onLoginSuccess,
        loginState
    )
}

@Composable
fun LoginScreenContent(
    onLogin: (String, String) -> Unit,
    onCreate: (String, String) -> Unit,
    onLoginSuccess: () -> Unit,
    loginState: LoginViewModel.LoginState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showToastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(showToastMessage) {
        showToastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginViewModel.LoginState.Success -> {
                Toast.makeText(context, "Successfully logged in! :)", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }

            is LoginViewModel.LoginState.Error -> {
                val errorMessage = loginState.message
                Toast.makeText(context, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign-In",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Please fill the form to continue",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { newValue ->
                email = newValue
            },
            placeholder = {
                Text(text = "E-mail")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { newValue ->
                password = newValue
            },
            placeholder = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Lock, contentDescription = null)
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onLogin(email, password) },
            enabled = loginState !is LoginViewModel.LoginState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (loginState is LoginViewModel.LoginState.Loading) "Logging in..." else "Sign-in",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(4.dp)
            )
            if (loginState is LoginViewModel.LoginState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "or create an account",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onCreate(email, password) },
            enabled = loginState !is LoginViewModel.LoginState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sign-up",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    TaskyUITheme {
        LoginScreenContent({ _, _ -> }, { _, _ -> }, {}, LoginViewModel.LoginState.Idle)
    }
}
