package com.hackware.mormont.notebook


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hackware.mormont.notebook.adapters.NoteListAdapter
import com.hackware.mormont.notebook.adapters.SearchNoteListAdapter
import com.hackware.mormont.notebook.db.NotesDataBase
import com.hackware.mormont.notebook.db.entity.NotesData
import org.wordpress.android.util.ToastUtils.showToast
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private var mDb: NotesDataBase? = null
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager :RecyclerView.LayoutManager
    private lateinit var mAdapter: NoteListAdapter
    private lateinit var mSearchAdapter: SearchNoteListAdapter
    private lateinit var mFab : View
    private lateinit var mItems: ArrayList<NotesData>
    private lateinit var mDbWorkerThread: DbWorkerThread
    private lateinit var pLayout :String

    private val mUiHandler = Handler() // UI хэндлер для удобной работы с потоком БД

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        mFab = findViewById(R.id.fab)
        mDbWorkerThread = DbWorkerThread("dbWorkerThread") // Создание дб Воркера
        mDbWorkerThread.start()

        getPref()
        recyclerView = findViewById(R.id.recycleView)
        setViewManager()
        recyclerView.layoutManager = viewManager

        mDb = NotesDataBase.getInstance(this) //  Получение инстанса БД
        loadDataInListView()

        mFab.setOnClickListener { view ->
            startActivity(Intent(this, Editor::class.java))
        }
        setRecyclerViewItemTouchListener()

    }

    override fun onStart() {
        getPref()
        super.onStart()
    }

    /**
     * Изменение интефейса в зависимости от настроек приложения
     */
    private fun setViewManager(){
        when(pLayout){

            "0" -> viewManager = LinearLayoutManager(this)
            "1" -> viewManager = GridLayoutManager(this, 2)
            else -> viewManager = LinearLayoutManager(this)
        }
        recyclerView.layoutManager = viewManager
    }

    override fun onRestart() {
        getPref()
        setViewManager()
        super.onRestart()
    }

    /**
     *  Получение настроек приложения
     */
    private fun getPref(){
        val mySharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        pLayout = mySharedPref.getString("layout_list", "0")!!
    }

    /**
     * Назначение листенера на RecyclerView, создание кэлбэка на свайпы
     */
    private fun setRecyclerViewItemTouchListener() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                toDeleteElementFromDb(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Удаление Note с БД
     */
    private fun toDeleteElementFromDb(position: Int) {
        mDbWorkerThread.postTask(Runnable{
            mDb?.notesDataDao()?.deleteNote(mItems[position])
            mUiHandler.post(Runnable {
                mItems.removeAt(position)
                recyclerView.adapter!!.notifyItemRemoved(position)
            })
        })
    }

    override fun onResume() {
        super.onResume()
        loadDataInListView()
    }
    /**
     * Подключение к туллбару меню
     */
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
                R.id.action_search -> {
                    searchView  = item.actionView as SearchView
                    searchQueryTextListener(searchView)
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    /**
     * Назначение листенера на поисковик
     */
    private fun searchQueryTextListener(searchView: SearchView): Boolean {
        searchView.queryHint = "Search notes"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                getNotesFromDb(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                getNotesFromDb(query)
                showToast(applicationContext, "Query Post")
                return true
            }
        })
        return  true
    }

    /**
     * Поиск заметки в БД, достать и положить в адаптер
     */
    fun getNotesFromDb(query: String){
        val searchTextQuery = "%$query%"
        mDb!!.notesDataDao().getNotesForQuery(searchTextQuery)
            .observe(this,
                Observer<List<NotesData>> { notes ->
                    mItems = ArrayList(notes)
                    mSearchAdapter = SearchNoteListAdapter(mItems)
                    recyclerView.adapter = mSearchAdapter
                })
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
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return  true}
            else -> true
        }
    }

    /**
     * Выгрузить с БД все заметки, достать и положить в адаптер
     */
    private fun loadDataInListView(){
        val task = Runnable {
            val notesData = mDb?.notesDataDao()?.getAll()
            mUiHandler.post {
                if (notesData == null || notesData.isEmpty()){
                    showToast(this, "NoData in Cashe...")
                } else{
                    mItems = ArrayList(notesData)
                    mAdapter = NoteListAdapter(mItems)
                    recyclerView.adapter = mAdapter
                }
            }
        }
        mDbWorkerThread.postTask(task)
    }

}
