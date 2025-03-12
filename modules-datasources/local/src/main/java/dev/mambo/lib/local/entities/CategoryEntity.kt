package dev.mambo.lib.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryEntity.TABLE_NAME)
data class CategoryEntity(
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {
    companion object {
        const val TABLE_NAME = "categories"
    }
}
