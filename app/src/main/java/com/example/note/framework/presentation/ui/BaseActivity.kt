package com.example.note.framework.presentation.ui

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

//    @Inject
//    lateinit var settingDataStore: SettingDataStore

//    val appTheme = mutableStateOf(Theme.DARK)

    override fun onStart() {
        super.onStart()
//        lifecycleScope.launchWhenStarted {
//            settingDataStore.settingFlow.collect { setting ->
//                appTheme.value = setting.theme
//            }
//        }
    }
}