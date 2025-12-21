
package com.reclaim.reclaim.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.reclaim.reclaim.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    /**
     * Upsert = (UP)date or in(SERT).
     * If a user with the given ID exists, it's updated. If not, it's inserted.
     * This is perfect for saving changes to the single user profile.
     */
    @Upsert
    suspend fun saveUser(user: UserEntity)

    /**
     * Gets the single user profile from the table.
     * We use Flow<> so the UI can automatically update when the user data changes in the database.
     */
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUser(): Flow<UserEntity?>
}
