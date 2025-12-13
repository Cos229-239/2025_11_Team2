package com.reclaim.reclaim.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

data class UserProfile(
    val uid: String,
    val email: String,
    val soberStartDate: LocalDate? = null,
    val affirmations: List<String> = emptyList()
)

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private val users = db.collection("users")

    suspend fun signUp(email: String, password: String): Result<UserProfile> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(IllegalStateException("No UID"))

            val profileDoc = mapOf(
                "email" to email,
                "soberStartDate" to null,               // store as epoch day Long or null
                "affirmations" to emptyList<String>()   // store as array
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
            val emailFromDb = snap.getString("email") ?: email
            val epochDay = snap.getLong("soberStartDate")  // may be null
            val affirmations = (snap.get("affirmations") as? List<*>)?.filterIsInstance<String>().orEmpty()

            val soberStart = epochDay?.let { LocalDate.ofEpochDay(it) }
            Result.success(UserProfile(uid, emailFromDb, soberStart, affirmations))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSoberStart(uid: String, date: LocalDate): Result<Unit> {
        return try {
            val userDoc = FirebaseFirestore.getInstance().collection("users").document(uid)
            userDoc.update("soberStart", date.toString()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun currentUid(): String? = auth.currentUser?.uid
}
