package com.example.notes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notes.presentation.screens.creation.CreateNoteScreen
import com.example.notes.presentation.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                CreateNoteScreen()
//                NotesScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity", "onNoteClick $it")
//                    },
//                    onAddNoteClick = {
//                        Log.d("MainActivity", "onFABClick")
//                    }
//                )
            }
        }
    }
}
