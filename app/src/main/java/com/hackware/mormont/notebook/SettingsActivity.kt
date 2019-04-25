package com.hackware.mormont.notebook


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.preference.Preference
import android.util.Log


/*
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_custom)

        initSettingFragment()
        setupActionBar()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */

    private fun setupActionBar() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun sendFB() {
        val email: Intent = Intent(android.content.Intent.ACTION_SEND)
        email.setType("plain/text")
        email.putExtra(android.content.Intent.EXTRA_EMAIL, R.string.dev_email)
        startActivity(Intent.createChooser(email,"Send email"))
    }

    private fun initSettingFragment(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_main, SettingsFragment())
            .commit()
    }
}



