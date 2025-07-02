package com.example.notes.presentation.screens.edititng

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.DeleteNoteUseCase
import com.example.notes.domain.EditNoteUseCase
import com.example.notes.domain.GetNoteUseCase
import com.example.notes.domain.Note
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditingNoteViewModel.Factory::class)
class EditingNoteViewModel @AssistedInject constructor(
    @Assisted("noteId") private val noteId: Int,
    private val editNoteUseCase: EditNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<EditNoteState>(EditNoteState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId)
                EditNoteState.Editing(note)
            }
        }
    }

    fun processCommand(command: EditNoteCommand) {
        when (command) {
            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }

            is EditNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val newNote = previousState.note.copy(content = command.content)
                        previousState.copy(note = newNote)
                    } else {
                        previousState
                    }
                }
            }

            is EditNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Editing) {
                        val newNote = previousState.note.copy(title = command.title)
                        previousState.copy(note = newNote)
                    } else {
                        previousState
                    }
                }
            }

            EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditNoteState.Editing) {
                            val note = previousState.note
                            editNoteUseCase(note)
                            EditNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }

            EditNoteCommand.Delete -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditNoteState.Editing) {
                            val note = previousState.note
                            deleteNoteUseCase(note.id)
                            EditNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
           @Assisted("noteId") noteId: Int
        ): EditingNoteViewModel
    }
}

sealed interface EditNoteCommand {

    data class InputTitle(val title: String) : EditNoteCommand

    data class InputContent(val content: String) : EditNoteCommand

    data object Save : EditNoteCommand

    data object Back : EditNoteCommand

    data object Delete : EditNoteCommand
}

sealed interface EditNoteState {

    data object Initial : EditNoteState
    data class Editing(
        val note: Note
    ) : EditNoteState {

        val isSafeEnabled: Boolean
            get() = note.title.isNotBlank() && note.content.isNotBlank()
    }

    data object Finished : EditNoteState
}