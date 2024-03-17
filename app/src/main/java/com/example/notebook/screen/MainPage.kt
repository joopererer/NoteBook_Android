package com.example.notebook.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notebook.R
import com.example.notebook.data.Note
import com.example.notebook.ui.theme.NoteBookTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(modifier: Modifier = Modifier, notes: List<Note> = emptyList(),
             addClick:()->Unit={}, noteClick:(Note)->Unit={}, deleteClick:(Note)->Unit={}) {

    val nbNotes = notes.size
//    println("nbNotes:$nbNotes")

    var showDialog by remember { mutableStateOf(false) }
    var noteSelected by remember { mutableStateOf(Note()) }

    Scaffold (
        modifier = modifier,
        // top bar
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = stringResource(id = R.string.topbar_title, nbNotes))
                }
            )
        },
        // add button
        floatingActionButton = {
            FloatingActionButton(
                onClick =
                {
//                    navController?.navigate("editPage")
                    addClick()
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add" )
            }
        },
    ) {
        LazyColumn(
            modifier = modifier.padding(it)
        ) {
            items(notes) { note ->
                NoteRow(note, modifier,
                    onClick = {
                        noteClick(it)
                    },
                    onLongPress = {
                        noteSelected = it
                        showDialog = true
                    }
                )
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
        DeleteConfirmationDialog(
            showDialog = showDialog,
            onConfirm = {
                // delete
                deleteClick(noteSelected)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}

@Composable
fun NoteRow(note: Note, modifier: Modifier = Modifier, onClick:(Note)->Unit = {}, onLongPress:(Note)->Unit={}) {
    Column (
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                    }, onTap = {
                        onClick(note)
                    }, onDoubleTap = {
                    }, onLongPress = {
                        onLongPress(note)
                    })
            }
    ) {
        Text(
            fontSize = 19.sp,
            text = note.title
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.End,
            text = note.date
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(id = R.string.delete_dialog_title)) },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text(stringResource(id = R.string.delete_dialog_confirm))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text(stringResource(id = R.string.delete_dialog_cancel))
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun NoteRowPreview() {
    NoteBookTheme {
        NoteRow(
            Note(
                1,
                "Stage",
                "J'ai postulé un stage ajourd'hui.",
                "07/03/2024"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    NoteBookTheme {
        MainPage(Modifier.fillMaxSize())
    }
}