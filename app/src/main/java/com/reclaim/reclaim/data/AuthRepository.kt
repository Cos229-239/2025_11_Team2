package com.reclaim.reclaim.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
// FIX: The import now correctly points to the new file we created.
import com.reclaim.reclaim.data.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

// The UserProfile data class is fine here for now.
data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val beforePictureUrl: String? = null,
    val currentPictureUrl: String? = null,
    val soberStartDate: Long? = null,
    val affirmations: List<String> = emptyList()
)

// FIX: The old, commented-out JournalEntry data class is COMPLETELY REMOVED from this file.

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val users = db.collection("users")

    // --- JOURNAL FUNCTIONS ---

    fun getJournalEntries(userId: String): Flow<List<JournalEntry>> {
        return db.collection("users").document(userId)
            .collection("journal_entries")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(JournalEntry::class.java)
            }
    }

    suspend fun addJournalEntry(userId: String, entry: JournalEntry): Result<Unit> {
        return try {
            val journalCollectionRef = db.collection("users").document(userId).collection("journal_entries")

            // 1. Add the new entry to the collection. Firestore auto-generates an ID.
            val documentReference = journalCollectionRef.add(entry).await()

            // 2. (Recommended) Update the newly created document to store its own ID.
            // This makes future lookups or deletions much easier.
            documentReference.update("id", documentReference.id).await()

            Result.success(Unit)
        } catch (e: Exception) {
            // This is crucial for debugging. If anything goes wrong,
            // the error will be printed in your Logcat window.
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteJournalEntry(userId: String, entryId: String): Result<Unit> {
        return try {
            db.collection("users").document(userId)
                .collection("journal_entries").document(entryId)
                .delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- OTHER REPOSITORY FUNCTIONS ---

    suspend fun signUp(email: String, password: String): Result<UserProfile> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(IllegalStateException("No UID"))

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
            Result.success(UserProfile(uid = uid, email = email))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<UserProfile> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(IllegalStateException("No UID"))
            val snap = users.document(uid).get().await()
            if (!snap.exists()) {
                return Result.failure(IllegalStateException("User profile not found in database."))
            }
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

    suspend fun updateImageUrl(uid: String, url: String, imageType: String): Result<Unit> {
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
            val fileName = fileUri.lastPathSegment ?: "image"
            val storageRef = storage.reference.child("uploads/$userId/$fileName")
            storageRef.putFile(fileUri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            println("Error uploading file: ${e.message}")
            null
        }
    }

    suspend fun updateUserName(userId: String, newName: String): Result<Unit> {
        return try {
            db.collection("users").document(userId)
                .update("name", newName)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSoberStart(uid: String, date: LocalDate): Result<Unit> {
        return try {
            val dateAsEpochDay = date.toEpochDay()
            db.collection("users").document(uid)
                .update("soberStartDate", dateAsEpochDay)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun currentUid(): String? = auth.currentUser?.uid

    fun logout() {
        auth.signOut()
    }
}
