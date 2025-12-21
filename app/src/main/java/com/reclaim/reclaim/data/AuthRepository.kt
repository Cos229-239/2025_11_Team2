package com.reclaim.reclaim.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

// FIX #1: Clean up the UserProfile data class.
// - Use standard Kotlin camelCase for property names.
// - Remove duplicate and commented-out properties.
data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val name: String? = null,
    val profilePictureUrl: String? = null, // Renamed for consistency
    val beforePictureUrl: String? = null,  // Renamed for consistency
    val currentPictureUrl: String? = null, // Renamed for consistency
    val soberStartDate: Long? = null,      // Store as Long (epochDay) for Firestore
    val affirmations: List<String> = emptyList()
)

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val users = db.collection("users")

    suspend fun signUp(email: String, password: String): Result<UserProfile> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(IllegalStateException("No UID"))

            // FIX #2: Use consistent keys for the Firestore document.
            // These keys now match the properties in the cleaned-up UserProfile data class.
            val profileDoc = mapOf(
                "uid" to uid,
                "email" to email,
                "name" to null,
                "soberStartDate" to null,
                "affirmations" to emptyList<String>(),
                "profilePictureUrl" to null,
                "beforePictureUrl" to null,
                "currentPictureUrl" to null
            )
            users.document(uid).set(profileDoc).await()

            // Return a valid UserProfile object on success
            Result.success(UserProfile(uid = uid, email = email))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<UserProfile> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(IllegalStateException("No UID"))

            // FIX #3: Rely on toObject() for cleaner data mapping.
            // This simplifies fetching and reduces errors.
            val snap = users.document(uid).get().await()
            if (!snap.exists()) {
                return Result.failure(IllegalStateException("User profile not found in database."))
            }

            // Let Firestore map the document directly to your clean UserProfile data class
            val userProfile = snap.toObject(UserProfile::class.java)
                ?: return Result.failure(IllegalStateException("Failed to parse user profile."))

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): Result<UserProfile> {
        return try {
            val snap = users.document(uid).get().await()
            if (!snap.exists()) {
                return Result.failure(IllegalStateException("User profile does not exist."))
            }
            val userProfile = snap.toObject(UserProfile::class.java)
                ?: return Result.failure(Exception("Failed to parse user profile."))
            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- This part of your code is already correct ---
    suspend fun updateImageUrl(uid: String, url: String, imageType: String): Result<Unit> {
        // Determine the correct Firestore field name based on the imageType
        val fieldName = when (imageType) {
            "profile" -> "profilePictureUrl"
            "before" -> "beforePictureUrl"
            "current" -> "currentPictureUrl"
            else -> return Result.failure(IllegalArgumentException("Invalid image type: $imageType"))
        }

        return try {
            users.document(uid).update(fieldName, url).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadFileToStorage(userId: String, fileUri: Uri): String? {
        return try {
            // Create a reference to a unique path in Firebase Storage
            // e.g., "uploads/userId/fileName.jpg"
            val fileName = fileUri.lastPathSegment ?: "image"
            val storageRef = storage.reference.child("uploads/$userId/$fileName")

            // Upload the file
            storageRef.putFile(fileUri).await()

            // Get the public download URL
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            // Log the exception or handle the error appropriately
            println("Error uploading file: ${e.message}")
            null
        }
    }
    suspend fun updateUserName(userId: String, newName: String): Result<Unit> {
        return try {
            // Get a reference to the user's document in the "users" collection
            db.collection("users").document(userId)
                // Update the "name" field with the new value
                .update("name", newName)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            // If there's an error, return a failure Result with the exception
            Result.failure(e)
        }
    }

    suspend fun updateSoberStart(uid: String, date: LocalDate): Result<Unit> {
        return try {
            // Convert LocalDate to a format Firestore understands, like a long (Epoch Day)
            val dateAsEpochDay = date.toEpochDay()

            // Get the reference to the user's document and update the field
            db.collection("users").document(uid) // <-- FIX: Changed 'firestore' to 'db'
                .update("soberStartDate", dateAsEpochDay)
                .await() // Wait for the Firestore operation to complete

            Result.success(Unit)
        } catch (e: Exception) {
            // If there's an error, return a failure Result with the exception
            Result.failure(e)
        }
    }
    // --- Other functions ---
    fun currentUid(): String? = auth.currentUser?.uid

    fun logout() {
        auth.signOut()
    }
}
