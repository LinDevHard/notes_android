package com.hackware.mormont.notebook.db.dao

import androidx.room.*
import com.hackware.mormont.notebook.db.entity.NotesData

@Dao
interface NotesDataDao{

    @Query("SELECT * from NotesData")
    fun getAll() : List<NotesData>

    @Query ("SELECT * from NotesData WHERE id= :id LIMIT 1")
    fun loadNoteWithId(id: Int): NotesData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(notesdata: NotesData)

    @Update
    fun updateNote(note: NotesData)

    @Delete
    fun deleteNote(note: NotesData)
}