package com.hackware.mormont.notebook.adapters

import com.hackware.mormont.notebook.db.entity.NotesData

class SearchNoteListAdapter(var item: ArrayList<NotesData>) :NoteListAdapter(item){
    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemId(position: Int): Long {
        return item[position].noteId
    }
}