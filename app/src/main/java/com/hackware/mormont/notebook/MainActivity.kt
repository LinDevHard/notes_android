package com.hackware.mormont.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
           startActivity(Intent(this, Editor::class.java))
        }
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
}
