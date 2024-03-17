package com.example.notebook.data

import android.content.Context

class AppDataContainer(private val context: Context) {
    val notesRepository: NoteDao by lazy {
        AppDatabase.getDatabase(context).noteDao()
    }
}