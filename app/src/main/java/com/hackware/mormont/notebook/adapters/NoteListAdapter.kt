package com.hackware.mormont.notebook.adapters


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hackware.mormont.notebook.db.entity.NotesData
import com.hackware.mormont.notebook.R

class NoteListAdapter(private var activity: Activity, private var items: ArrayList<NotesData>): BaseAdapter() {

    private class ViewHolder(row: View?){
        var title: TextView? = null
        var noteContent: TextView? = null

        init {
            this.title = row?.findViewById(R.id.title)
            this.noteContent = row?.findViewById(R.id.noteContent)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder : ViewHolder

        if (convertView == null){
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.note_listview, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val noteData  = items[position]
        viewHolder.title?.text = noteData.title
        viewHolder.noteContent?.text =  noteData.strContent
        return view as View
    }

    override fun getItem(position: Int): NotesData{
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int{
        return  items.size
    }
}