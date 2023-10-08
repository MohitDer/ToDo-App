package com.hindustan.notesapp.Database

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String,
    var content: String,
    var priority: Int,
    var date: String
)
