package com.ardianeffendi.githubclient.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ardianeffendi.githubclient.R
import com.ardianeffendi.githubclient.ui.fragments.MySettingFragment

class SettingsActivity : AppCompatActivity() {

    private val settingsTitle = "Settings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Back Button on Action Bar
        val actionBar = supportActionBar
        actionBar?.title = settingsTitle
        actionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_fragment_container, MySettingFragment())
            .commit()
    }
}