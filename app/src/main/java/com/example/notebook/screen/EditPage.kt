package com.example.notebook.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notebook.MAIN_PAGE
import com.example.notebook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorPage(modifier: Modifier = Modifier, noteViewModel: NoteViewModel = viewModel(), navController: NavController? = null) {
    val note by noteViewModel.note.collectAsState()
    val isNewNote = note.id==0
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = if (isNewNote) {
                            stringResource(id = R.string.edit_page_title_new)
                        } else {
                            stringResource(id = R.string.edit_page_title_edit)
                        }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(15.dp)
        ) {
            // title
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Title:",
                    fontSize = 17.sp,
                    modifier = Modifier.padding(end=10.dp)
                )
                TextField(
                    value = note.title,
                    singleLine = true,
                    onValueChange = {
                        noteViewModel.updateTitle(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // content
            TextField(
                value = note.content,
                onValueChange = {
                    noteViewModel.updateContent(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 15.dp)
            )
            // buttons
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    noteViewModel.saveNote()
                    navController?.navigate(MAIN_PAGE) {
                        launchSingleTop = true
                    }
                }) {
                    Text(text = "Confirmer")
                }
                Spacer(modifier = Modifier.width(60.dp))
                Button(onClick = {
                    navController?.navigate(MAIN_PAGE) {
                        launchSingleTop = true
                    }
                }) {
                    Text(text = "Annuler")
                }
            }
        }
    }
}

@Preview
@Composable
fun EditorPagePreview(){
    EditorPage(Modifier.fillMaxSize())
}
