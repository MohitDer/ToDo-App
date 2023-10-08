package com.hindustan.notesapp.Database

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val priority: Int,
    val date: String
)
