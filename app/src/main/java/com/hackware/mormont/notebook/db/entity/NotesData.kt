package com.hackware.mormont.notebook.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "NotesData")
data class NotesData(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "note_id") var noteId: Int,
    @ColumnInfo(name = "created_date") var createdDate:Int,
    @ColumnInfo(name = "modified_date") var modifiedDate: Int,
    @ColumnInfo(name = "str_content") var strContent: String,
    @ColumnInfo(name = "content") var rawContent: String

){
    constructor():this(null, 0, 0,0,"","")
}