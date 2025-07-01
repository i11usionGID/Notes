package com.example.notes.domain

class AddNoteUseCase(
    private val repository: NotesRepository
) {

    suspend operator fun invoke(
        title: String,
        content: String
    ) {
        repository.addNote(
            title,
            content,
            updatedAt = System.currentTimeMillis(),
            isPinned = false
        )
    }
}
