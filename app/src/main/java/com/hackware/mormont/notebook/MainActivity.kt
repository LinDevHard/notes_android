package com.hackware.mormont.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ListView
import android.widget.PopupMenu
import com.hackware.mormont.notebook.adapters.NoteListAdapter
import com.hackware.mormont.notebook.db.NotesDataBase
import org.wordpress.android.util.ToastUtils.showToast

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private var mDb: NotesDataBase? = null

    private lateinit var mListView: ListView
    private lateinit var mAdapter: NoteListAdapter
    private lateinit var mFab : View

    private lateinit var  mDbWorkerThread: DbWorkerThread

    private val mUiHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        mFab = findViewById(R.id.fab)
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mListView = findViewById(R.id.listView)
        mDb = NotesDataBase.getInstance(this)

        mFab.setOnClickListener { view ->
           startActivity(Intent(this, Editor::class.java))
        }

        loadDataInListView()
    }

    override fun onResume() {
        super.onResume()
        loadDataInListView()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId){
                R.id.sett -> {
                    showPopup(findViewById(R.id.sett))
                    true
                }
                R.id.find -> {
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    private fun showPopup(v: View): Boolean{
        PopupMenu(this, v, Gravity.END).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.popup_menu)
            show()
        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean{
        return when (item.itemId){
            R.id.settings -> true
            else -> false
        }
    }

    private fun loadDataInListView(){
        val task = Runnable {
            val notesData = mDb?.notesDataDao()?.getAll()
            mUiHandler.post {
                if (notesData == null || notesData.size == 0){
                    showToast(this, "NoData in Cashe...")
                } else{
                    mAdapter = NoteListAdapter(this, ArrayList(notesData))
                    mListView.adapter = mAdapter
                }
            }
        }
        mDbWorkerThread.postTask(task)
    }
}
