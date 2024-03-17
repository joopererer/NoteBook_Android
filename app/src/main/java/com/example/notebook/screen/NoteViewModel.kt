package com.example.notebook.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.notebook.data.Note
import com.example.notebook.data.NoteDao
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteViewModel(private val db: NoteDao): ViewModel() {

    val mainScope = MainScope()

    private val _currentNote = MutableStateFlow(Note())
    val note: StateFlow<Note> = _currentNote.asStateFlow()

    private val _isNewNote = mutableStateOf(true)
    val isNewNote: Boolean = _isNewNote.value

    private val _notesList = mutableStateOf<List<Note>>(emptyList())
    var notesList: State<List<Note>> = _notesList

    fun setEditNote(note: Note) {
        _currentNote.value = note.copy()
        _isNewNote.value = false
    }

    fun updateTitle(title: String) {
        _currentNote.value = _currentNote.value.copy(title=title)
    }

    fun updateContent(content: String) {
        _currentNote.value = _currentNote.value.copy(content=content)
    }

    fun updateDate() {
        _currentNote.value = _currentNote.value.copy(date = getCurrentDate())
    }

    fun deleteNote(note: Note) {
        _notesList.value = _notesList.value.filter {
            it.id != note.id
        }
        mainScope.launch {
            db.deleteNote(note)
            _notesList.value = db.getAllNotes()
        }
    }

    fun saveNote() {
        mainScope.launch {
            val note = db.getNote(_currentNote.value.id)
            if(note==null) {
                db.insertNote(_currentNote.value.copy(date = getCurrentDate()))
            }else{
                db.updateNote(_currentNote.value.copy(id=note.id, date = getCurrentDate()))
            }
            _notesList.value = db.getAllNotes()
        }
    }

    fun getNotes(): List<Note> {
        mainScope.launch {
            _notesList.value = db.getAllNotes()
//            println("getNotes:${_notesList.value.size}")
        }
        return notesList.value
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}