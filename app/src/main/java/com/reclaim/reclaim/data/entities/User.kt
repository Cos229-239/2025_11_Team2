// In app/src/main/java/com/reclaim/reclaim/data/entity/User.kt
package com.reclaim.reclaim.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    // Use a fixed ID for a single-user profile to easily find it
    @PrimaryKey val id: Int = 1,

    val name: String,

    // Store the path (URI) to the image, not the image itself
    val profilePictureUri: String? = null
)


