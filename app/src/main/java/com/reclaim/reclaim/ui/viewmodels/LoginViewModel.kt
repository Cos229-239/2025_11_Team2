package com.reclaim.reclaim.ui.viewmodels

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.AuthRepository
import com.reclaim.reclaim.data.UserProfile
import com.reclaim.reclaim.ui.widget.SoberTimeWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    fun signUpUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            _loading.value = false
            result.onSuccess {
                _user.value = it
                onComplete(true)
            }.onFailure { e ->
                _error.value = e.message ?: "Sign up failed"
                onComplete(false)
            }
        }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loading.value = false
            result.onSuccess {
                _user.value = it
                onComplete(true)
            }.onFailure { e ->
                _error.value = e.message ?: "Login failed"
                onComplete(false)
            }
        }
    }

    fun saveSoberStart(context: Context, date: LocalDate) {
        viewModelScope.launch {
            val uid = authRepository.currentUid() ?: return@launch
            val result = authRepository.updateSoberStart(uid, date)
            result.onSuccess {
                SoberTimeWidget().updateAll(context)
            }.onFailure {
                // Optionally set _error for UI
            }
        }
    }
}