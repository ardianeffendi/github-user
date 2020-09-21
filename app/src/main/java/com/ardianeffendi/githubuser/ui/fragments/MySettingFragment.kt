package com.ardianeffendi.githubuser.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.ui.AlarmReceiver

class MySettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var alarmReceiver: AlarmReceiver

    private lateinit var REMINDER: String
    private lateinit var isReminderSet: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
        alarmReceiver = AlarmReceiver()
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminderSet.isChecked = sh.getBoolean(REMINDER, false)
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)
        isReminderSet = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == REMINDER) {
            val isChecked = sharedPreferences.getBoolean(REMINDER, false)
            val repeatMessage = "Find more Github's Users!"
            isReminderSet.isChecked = isChecked
            if (isChecked) {
                activity?.applicationContext?.let {
                    alarmReceiver.setRepeatingAlarm(
                        it,
                        AlarmReceiver.TYPE_REPEATING,
                        "09:00",
                        repeatMessage
                    )
                }
            } else {
                activity?.applicationContext?.let {
                    alarmReceiver.cancelAlarm(it, AlarmReceiver.TYPE_REPEATING)
                }
            }

        }
    }


}