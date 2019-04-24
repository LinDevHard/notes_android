package com.hackware.mormont.notebook.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hackware.mormont.notebook.db.entity.NotesData

@Dao
interface NotesDataDao{

    @Query("SELECT * from NotesData")
    fun getAll() : List<NotesData>

    @Query ("SELECT * from NotesData WHERE note_id= :id LIMIT 1")
    fun loadNoteWithId(id: Long): NotesData

    @Query("SELECT * FROM NotesData  WHERE content LIKE :query")
    fun getNotesForQuery(query: String): LiveData<List<NotesData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NotesData)

    @Update
    fun updateNote(note: NotesData)

    @Delete
    fun deleteNote(note: NotesData)
}