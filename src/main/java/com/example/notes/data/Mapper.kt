package com.example.notes.data

import com.example.notes.domain.Note

fun Note.toDbModel() = NoteDbModel(
    id,
    title,
    content,
    updatedAt,
    isPinned
)

fun NoteDbModel.toEntity() = Note(
    id,
    title,
    content,
    updatedAt,
    isPinned
)