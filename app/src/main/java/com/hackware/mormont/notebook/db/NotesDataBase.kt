package com.hackware.mormont.notebook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hackware.mormont.notebook.db.convertors.DateConverter
import com.hackware.mormont.notebook.db.dao.NotesDataDao
import com.hackware.mormont.notebook.db.entity.NotesData

@Database(entities = [NotesData::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class NotesDataBase: RoomDatabase() {

    abstract fun  notesDataDao(): NotesDataDao

    companion object {
        private var INSTANCE: NotesDataBase? = null

        fun getInstance(context: Context) : NotesDataBase? {
            if (INSTANCE == null) {
                synchronized(NotesDataBase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            NotesDataBase::class.java, "notes.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }

    }
}