package com.reclaim.reclaim.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.daos.UserDao
import com.reclaim.reclaim.data.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class representing the state of the Profile screen.
 * This holds all the information the UI needs to display.
 */
data class ProfileUiState(
    val userName: String = "",
    val isEditingName: Boolean = false,
    val profilePictureUri: String? = null,
    val BeforePictureUri: String? = null,
    val CurrentPictureUri: String? = null
)

/**
 * The ViewModel for the ProfileScreen.
 * - Annotated with @HiltViewModel to allow Hilt to create and inject its dependencies.
 * - It connects the UI to the data layer (UserRepository/UserDao).
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao // Hilt provides this automatically from your AppModule.
) : ViewModel() {

    // Private mutable state flow that only the ViewModel can modify.
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Public, read-only state flow that the UI can observe for changes.
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // When the ViewModel is first created, start loading the user profile from the database.
        loadUserProfile()
    }

    /**
     * Observes the user data from the Room database using a Flow.
     * Whenever the user data changes in the database, this will automatically
     * trigger and update the UI state.
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            userDao.getUser().collect { userFromDb ->
                _uiState.update { currentState ->
                    currentState.copy(
                        // If the user is null (e.g., first time app runs), provide a default name.
                        userName = userFromDb?.name ?: "Enter Your Name",
                        profilePictureUri = userFromDb?.profilePictureUri
                    )
                }
            }
        }
    }

    /**
     * Called by the UI whenever the user types in the name text field.
     * Updates the UI state in memory.
     */
    fun onNameChange(newName: String) {
        _uiState.update { it.copy(userName = newName) }
    }


    /**
     * Toggles the name editing mode. If the user is finishing an edit (isEditing becomes false),
     * it triggers the save operation.
     */
    fun onEditModeChange(isEditing: Boolean) {
        // If we are exiting edit mode, save the changes to the database.
        if (!isEditing) {
            saveUserProfile()
        }
        _uiState.update { it.copy(isEditingName = isEditing) }
    }

    /**
     * Saves the current UI state (name and picture URI) to the Room database.
     */
    private fun saveUserProfile() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updatedUser = User(
                id = 1, // Use a fixed ID for the single user profile
                name = currentState.userName,
                profilePictureUri = currentState.profilePictureUri
            )
            // @Upsert in the DAO handles both inserting a new user and updating an existing one.
            userDao.saveUser(updatedUser)
        }
    }

    /**
     * Called by the UI when the user selects a new profile picture.
     * Updates the URI in the state and immediately saves the change.
     */
    fun onProfilePictureChanged(newUri: Uri?) {
        _uiState.update { it.copy(profilePictureUri = newUri?.toString()) }
        // Save immediately after changing the picture.
        saveUserProfile()
    }
}
