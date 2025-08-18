package com.example.notes.di

import android.content.Context
import androidx.room.Room
import com.example.notes.data.NotesDao
import com.example.notes.data.NotesDataBase
import com.example.notes.data.NotesRepositoryImpl
import com.example.notes.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNotesRepositoryImpl(
        impl: NotesRepositoryImpl
    ): NotesRepository

    companion object {
        @Singleton
        @Provides
        fun provideDataBase(
            @ApplicationContext context: Context
        ): NotesDataBase {
            return Room.databaseBuilder(
                context = context,
                klass = NotesDataBase::class.java,
                name = "notes.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Singleton
        @Provides
        fun provideNotesDao(
            notesDataBase: NotesDataBase
        ): NotesDao {
            return notesDataBase.notesDao()
        }
    }
}