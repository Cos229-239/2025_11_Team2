package com.reclaim.reclaim.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.reclaim.reclaim.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// This state class MUST match what the UI needs to display.
data class ProfileUiState(
    val userName: String = "",
    val isEditingName: Boolean = false,
    val profilePictureUrl: String? = null,
    val beforePictureUrl: String? = null,
    val currentPictureUrl: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val currentUserId: String? get() = auth.currentUser?.uid

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            authRepository.getUserProfile(userId).onSuccess { profile ->
                _uiState.update {
                    it.copy(
                        userName = profile.name ?: "",
                        profilePictureUrl = profile.profilePictureUrl,
                        beforePictureUrl = profile.beforePictureUrl,
                        currentPictureUrl = profile.currentPictureUrl
                    )
                }
            }
        }
    }

    /**
     * This is the function called by the UI when a new image is selected.
     * It orchestrates the entire upload and update process.
     */
    fun onImageChanged(newUri: Uri?, imageType: String) {
        val userId = currentUserId ?: return
        if (newUri == null) return // Do nothing if the URI is null

        viewModelScope.launch {
            // Step 1: Upload the image file to Storage and get a URL string back.
            val downloadUrl = authRepository.uploadFileToStorage(userId, newUri)

            if (downloadUrl != null) {
                // Step 2: Save the new URL string to the correct field in Firestore.
                authRepository.updateImageUrl(userId, downloadUrl, imageType)

                // Step 3: CRUCIAL FIX - This updates the UI state with the new URL.
                // If this block is missing, the UI will NEVER show the new image.
                _uiState.update { currentState ->
                    when (imageType) {
                        "profile" -> currentState.copy(profilePictureUrl = downloadUrl)
                        "before" -> currentState.copy(beforePictureUrl = downloadUrl)
                        "current" -> currentState.copy(currentPictureUrl = downloadUrl)
                        else -> currentState // Return unchanged state if type is unknown
                    }
                }
            } else {
                // Optional: Handle the upload failure (e.g., show a toast message)
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(userName = newName) }
    }

    fun onEditModeChange(isEditing: Boolean) {
        if (uiState.value.isEditingName && !isEditing) {
            saveUserName()
        }
        _uiState.update { it.copy(isEditingName = isEditing) }
    }

    private fun saveUserName() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            authRepository.updateUserName(userId, uiState.value.userName)
        }
    }
}
