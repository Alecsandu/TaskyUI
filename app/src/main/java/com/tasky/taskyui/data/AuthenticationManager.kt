package com.tasky.taskyui.data

import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthenticationManager {
    private val auth = Firebase.auth

    fun createAccountWithEmail(email: String, password: String): Flow<AuthenticationResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthenticationResponse.Success)
                } else {
                    trySend(AuthenticationResponse.Error(msg = task.exception?.message ?: "Account creation failed"))
                }
            }
        awaitClose()
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthenticationResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthenticationResponse.Success)
                } else {
                    trySend(AuthenticationResponse.Error(msg = task.exception?.message ?: "Login failed"))
                }
            }
        awaitClose()
    }
}

