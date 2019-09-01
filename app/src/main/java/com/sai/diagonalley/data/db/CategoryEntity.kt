package com.sai.diagonalley.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "is_selected") var isSelected: Boolean
)
