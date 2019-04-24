package com.hackware.mormont.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import com.hackware.mormont.notebook.db.NotesDataBase
import com.hackware.mormont.notebook.db.entity.NotesData
import com.hackware.mormont.notebook.utils.DateUtil
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.AztecText
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.toolbar.AztecToolbar
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import kotlin.random.Random



open class Editor : AppCompatActivity(),
    IAztecToolbarClickListener {

    protected lateinit var aztec: Aztec

    private lateinit var title: EditText
    private lateinit var mDbWorkerThread: DbWorkerThread
    private var mDb: NotesDataBase? = null
    private val INTENT_NOTE_ID: String = "NoteId"
    private var isEditing = false
    private lateinit var mData: NotesData

    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        mDbWorkerThread = DbWorkerThread("dbWorkerEditorThread")
        mDbWorkerThread.start()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        title = findViewById(R.id.title)
        mDb = NotesDataBase.getInstance(this)

        val visualEditor = findViewById<AztecText>(R.id.aztec)
        val toolbar = findViewById<AztecToolbar>(R.id.formatting_toolbar)

        aztec = Aztec.with(visualEditor, toolbar, this)

        val noteId: Long= intent.getLongExtra(INTENT_NOTE_ID, 0.toLong())
        if( noteId != 0.toLong()){
            isEditing = true
            loadDataFromBd(noteId)
        }
    }

    override fun onBackPressed() {
        var data: NotesData
        if (isEditing){
            if (mData.title != title.text.toString() || mData.rawContent != aztec.visualEditor.toFormattedHtml() )
                saveEditedData()
        }else  {
            if (!title.text.isEmpty()) {
                data = NotesData(
                    Random.nextLong(),
                    Random.nextLong(), DateUtil.getCurrentDate(), DateUtil.getCurrentDate(),
                    title.text.toString(),
                    aztec.visualEditor.text.toString(),
                    aztec.visualEditor.toFormattedHtml()
                )

                val task = Runnable {
                    mDb?.notesDataDao()?.insertNote(data)
                }
                mDbWorkerThread.postTask(task)
            }
        }

        return super.onBackPressed()
    }

    private fun loadDataFromBd(id: Long){
        val task = Runnable {
               mData = mDb!!.notesDataDao().loadNoteWithId(id)
            mUiHandler.post{
                if (mData.noteId == id){
                    title.setText(mData.title)
                    aztec.visualEditor.fromHtml(mData.rawContent)
                }
            }
        }
        mDbWorkerThread.postTask(task)
    }

    private  fun saveEditedData(){
        mData.title = title.text.toString()
        mData.strContent = aztec.visualEditor.text.toString()
        mData.rawContent = aztec.visualEditor.toFormattedHtml()
        mData.modifiedDate = DateUtil.getCurrentDate()

        val task = Runnable {
            mDb?.notesDataDao()?.updateNote(mData)
        }
        mDbWorkerThread.postTask(task)
    }


    override fun onToolbarCollapseButtonClicked() {
    }

    override fun onToolbarExpandButtonClicked() {
    }

    override fun onToolbarFormatButtonClicked(format: ITextFormat, isKeyboardShortcut: Boolean) {
    }

    override fun onToolbarHeadingButtonClicked() {
    }

    override fun onToolbarHtmlButtonClicked() {
    }

    override fun onToolbarListButtonClicked() {
    }

    override fun onToolbarMediaButtonClicked(): Boolean {
        return false
    }
}


