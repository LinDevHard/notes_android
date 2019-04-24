package com.hackware.mormont.notebook.adapters

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hackware.mormont.notebook.Editor
import com.hackware.mormont.notebook.db.entity.NotesData
import com.hackware.mormont.notebook.R
import com.hackware.mormont.notebook.utils.inflate
import com.hackware.mormont.notebook.utils.prepareString
import com.hackware.mormont.notebook.utils.toSimpleString
import java.util.*

open class NoteListAdapter(var items: ArrayList<NotesData>):
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflaterView = parent.inflate(R.layout.note_listview, false)
        return ViewHolder(inflaterView)
    }

    override fun getItemCount(): Int = items.size
    override fun getItemId(position: Int): Long {
        return 14
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title?.text = items[position].title.prepareString(80)
        holder.noteContent?.text = items[position].strContent.prepareString(180)
        holder.noteId = items[position].noteId
        holder.date.text = items[position].createdDate.toSimpleString("dd.MM.yy 'at' HH:mm")
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row), View.OnClickListener{
        private  var view: View = row

        var title: TextView? = null
        var noteContent: TextView? = null
        var noteId: Long = 0
        var date : TextView
        init {
            view.setOnClickListener(this)
            this.title = row.findViewById(R.id.title)
            this.noteContent = row.findViewById(R.id.noteContent)
            this.date = row.findViewById(R.id.date)
        }

        override fun onClick(v: View?) {
            Log.d("RecyclerView", "CLICK!")
            newIntent(noteId)
        }

        private fun  newIntent(id: Long): Boolean{
            val context = itemView.context
            val intent: Intent = Intent(context, Editor::class.java).apply {
                putExtra("NoteId", id)
            }
            context.startActivity(intent)
            return true
        }
    }
}