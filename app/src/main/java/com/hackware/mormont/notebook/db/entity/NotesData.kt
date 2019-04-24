package com.hackware.mormont.notebook.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hackware.mormont.notebook.db.convertors.DateConverter
import java.util.*

@Entity(tableName= "NotesData")
@TypeConverters(DateConverter::class)
data class NotesData(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "note_id") var noteId: Long,
    @ColumnInfo(name = "created_date") var createdDate: Date,
    @ColumnInfo(name = "modified_date") var modifiedDate: Date,
    @ColumnInfo(name = "title") var  title: String,
    @ColumnInfo(name = "str_content") var strContent: String,
    @ColumnInfo(name = "content") var rawContent: String

){
    constructor():this(null, 0, Date(0),Date(0),"","", "")
}