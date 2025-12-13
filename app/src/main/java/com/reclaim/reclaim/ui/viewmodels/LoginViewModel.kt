package com.reclaim.reclaim.ui.viewmodels

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.AuthRepository
import com.reclaim.reclaim.data.UserProfile
import com.reclaim.reclaim.ui.viewmodels.HomeViewModel.PreferencesKeys
import com.reclaim.reclaim.ui.widget.SoberTimeWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    fun loginUser(email: String, password: String, context: Context, onComplete: (Boolean) -> Unit) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loading.value = false
            result.onSuccess {
                _user.value = it
                syncSoberStart(context)
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

    fun syncSoberStart(context: Context) {
        viewModelScope.launch {
            val uid = authRepository.currentUid() ?: return@launch
            try {
                val snapshot = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .get()
                    .await()

                val dateString = snapshot.getString("soberStart")
                if (dateString != null) {
                    val parsed = LocalDate.parse(dateString)
                    context.dataStore.edit { prefs ->
                        prefs[PreferencesKeys.SOBER_START_DATE] = parsed.toEpochDay()
                    }
                    SoberTimeWidget().updateAll(context)
                }
            } catch (e: Exception) {
                // Optionally expose error to UI
                println("Failed to sync sober start: ${e.message}")
            }
        }
    }

    fun clearSoberStart(context: Context) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs.remove(PreferencesKeys.SOBER_START_DATE)
            }
            SoberTimeWidget().updateAll(context)
        }
    }

    fun logout(context: Context, onComplete: (Boolean) -> Unit = {}) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                authRepository.logout()   // sign out from Firebase/Auth
                clearSoberStart(context)  // wipe local sober date
                _user.value = null        // reset user state
                _loading.value = false
                onComplete(true)
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Logout failed"
                onComplete(false)
            }
        }
    }

}