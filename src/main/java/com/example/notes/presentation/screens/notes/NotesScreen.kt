@file:OptIn(ExperimentalFoundationApi::class)

package com.example.notes.presentation.screens.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.domain.Note
import com.example.notes.presentation.ui.theme.Green
import com.example.notes.presentation.ui.theme.Yellow200

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Title(text = "All notes")
        }
        item {
            SearchBar(
                query = state.query,
                onQueryChanged = {
                    viewModel.processCommand(NotesCommand.InputSearchQuery(it))
                }
            )
        }
        item {
            SubTitle(text = "Pinned")
        }
        item {
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    state.pinnedNotes,
                    key = { it.id }
                ) { note ->
                    NoteCard(
                        modifier = modifier.fillMaxWidth(),
                        note = note,
                        onNoteClick = {
                            viewModel.processCommand(NotesCommand.EditNote(it))
                        },
                        onLongClick = {
                            viewModel.processCommand(NotesCommand.SwitchedPinnedStatus(it.id))
                        },
                        onDoubleClick = {
                            viewModel.processCommand(NotesCommand.DeleteNote(it.id))
                        },
                        backgroundColor = Yellow200
                    )
                }
            }
        }
        item {
            SubTitle(
                text = "Others"
            )
        }
        items(
            state.otherNotes,
            key = { it.id }
        ) { note ->
            NoteCard(
                modifier.fillMaxWidth(),
                note = note,
                onNoteClick = {
                    viewModel.processCommand(NotesCommand.EditNote(it))
                },
                onLongClick = {
                    viewModel.processCommand(NotesCommand.SwitchedPinnedStatus(it.id))
                },
                onDoubleClick = {
                    viewModel.processCommand(NotesCommand.DeleteNote(it.id))
                },
                backgroundColor = Green
            )
        }
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun SubTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Notes"
            )
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    note: Note,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    onDoubleClick: (Note) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                },
                onLongClick = {
                    onLongClick(note)
                },
                onDoubleClick = {
                    onDoubleClick(note)
                }
            )
    ) {
        Text(
            text = note.title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = note.updatedAt.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = note.content,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}