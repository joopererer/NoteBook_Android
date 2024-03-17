package com.example.notebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notebook.data.AppDataContainer
import com.example.notebook.data.Note
import com.example.notebook.screen.EditorPage
import com.example.notebook.screen.MainPage
import com.example.notebook.screen.NoteViewModel
import com.example.notebook.ui.theme.NoteBookTheme

class MainActivity : ComponentActivity() {

    private lateinit var container: AppDataContainer
    lateinit var model: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        container = AppDataContainer(this)
        model = NoteViewModel(container.notesRepository)
        enableEdgeToEdge()
        setContent {
            NoteBookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeCompose(Modifier.fillMaxSize(), model)
                }
            }
        }
    }
}

val MAIN_PAGE = "mainPage"
val EDIT_PAGE = "editPage"

@Composable
fun HomeCompose(modifier: Modifier = Modifier, noteViewModel: NoteViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MAIN_PAGE) {
        composable(MAIN_PAGE) {
            MainPage(modifier = modifier, notes = noteViewModel.getNotes(),
                addClick = {
                    noteViewModel.setEditNote(Note())
                    navController.navigate(EDIT_PAGE)
                },
                noteClick = {
                    noteViewModel.setEditNote(it)
                    navController.navigate(EDIT_PAGE)
                },
                deleteClick = {
                    noteViewModel.deleteNote(it)
                }
            )
        }
        composable(EDIT_PAGE) {
            EditorPage(modifier = modifier, navController = navController, noteViewModel = noteViewModel)
        }
    }
}