package dev.mambo.lib.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update

interface BaseDAO<T> {
    @Insert(onConflict = REPLACE)
    suspend fun insert(item: T): Long

    @Insert(onConflict = REPLACE)
    suspend fun insert(items: List<T>)

    @Update(onConflict = REPLACE)
    suspend fun update(item: T): Int

    @Update(onConflict = REPLACE)
    suspend fun update(items: List<T>)

    @Delete
    suspend fun delete(item: T)
}
