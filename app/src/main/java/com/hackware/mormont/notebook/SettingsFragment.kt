package com.hackware.mormont.notebook

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        val pref: Preference = findPreference("feedback")
        pref.onPreferenceClickListener = Preference.OnPreferenceClickListener{
            sendFB()
        }
    }

    private fun sendFB(): Boolean {
        val email: Intent = Intent(Intent.ACTION_SEND)
        email.setType("plain/text")
        email.putExtra(android.content.Intent.EXTRA_EMAIL, R.string.dev_email)
        email.putExtra(android.content.Intent.EXTRA_SUBJECT,
            "Notes - Feedback")
        startActivity(Intent.createChooser(email,"Send email"))
        return true
    }
}