package com.example.notes.data

import com.example.notes.domain.ContentItem
import com.example.notes.domain.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Note.toDbModel(): NoteDbModel {
    val contentAsString = Json.encodeToString(content.toContentItemDbModels())
    return NoteDbModel(
        id,
        title,
        contentAsString,
        updatedAt,
        isPinned
    )
}

fun List<ContentItem>.toContentItemDbModels(): List<ContentItemDbModel> {
    return map { contentItem ->
        when (contentItem) {
            is ContentItem.Image -> {
                ContentItemDbModel.Image(url = contentItem.url)
            }

            is ContentItem.Text -> {
                ContentItemDbModel.Text(content = contentItem.content)
            }
        }
    }
}

fun List<ContentItemDbModel>.toContentItems(): List<ContentItem> {
    return map { contentItem ->
        when (contentItem) {
            is ContentItemDbModel.Image -> {
                ContentItem.Image(url = contentItem.url)
            }

            is ContentItemDbModel.Text -> {
                ContentItem.Text(content = contentItem.content)
            }
        }
    }
}

fun NoteDbModel.toEntity(): Note {
    val contentItemDbModels = Json.decodeFromString<List<ContentItemDbModel>>(content)
    return Note(
        id,
        title,
        contentItemDbModels.toContentItems(),
        updatedAt,
        isPinned
    )
}

fun List<NoteDbModel>.toEntities(): List<Note> {
    return map { it.toEntity() }
}