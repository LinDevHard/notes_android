package com.hackware.mormont.notebook

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import org.wordpress.android.util.ToastUtils
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.AztecText
import org.wordpress.aztec.IHistoryListener
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.plugins.CssUnderlinePlugin
import org.wordpress.aztec.source.SourceViewEditText
import org.wordpress.aztec.toolbar.AztecToolbar
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import org.xml.sax.Attributes


open class Editor : AppCompatActivity(),
    View.OnTouchListener,
    IHistoryListener,
    IAztecToolbarClickListener {


    override fun onUndoEnabled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRedoEnabled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    protected lateinit var aztec: Aztec

    private var mIsKeyboardOpen = true
    private var mHideActionBarOnSoftKeyboardUp = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            // If the WebView or EditText has received a touch event, the keyboard will be displayed and the action bar
            // should hide
            mIsKeyboardOpen = true
            hideActionBarIfNeeded()
        }
        return false    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mHideActionBarOnSoftKeyboardUp = true
        }
        val visualEditor = findViewById<AztecText>(R.id.aztec)
        val toolbar = findViewById<AztecToolbar>(R.id.formatting_toolbar)


        aztec = Aztec.with(visualEditor, toolbar, this)
            .setOnTouchListener(this)

      //  aztec.visualEditor.setCalypsoMode(true)

        aztec.addPlugin(CssUnderlinePlugin())

    }

    override fun onPause() {
        super.onPause()
        mIsKeyboardOpen = false
    }

    override fun onResume() {
        super.onResume()

        showActionBarIfNeeded()
    }

    override fun onBackPressed() {
        mIsKeyboardOpen = false
        showActionBarIfNeeded()

        return super.onBackPressed()
    }

        private fun isHardwareKeyboardPresent(): Boolean {
            val config = resources.configuration
            var returnValue = false
            if (config.keyboard != Configuration.KEYBOARD_NOKEYS) {
                returnValue = true
            }
            return returnValue
        }

        private fun hideActionBarIfNeeded() {

            val actionBar = supportActionBar
            if (actionBar != null
                && !isHardwareKeyboardPresent()
                && mHideActionBarOnSoftKeyboardUp
                && mIsKeyboardOpen
                && actionBar.isShowing) {
                actionBar.hide()
            }
        }

        /**
         * Show the action bar if needed.
         */
        private fun showActionBarIfNeeded() {

            val actionBar = supportActionBar
            if (actionBar != null && !actionBar.isShowing) {
                actionBar.show()
            }
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


