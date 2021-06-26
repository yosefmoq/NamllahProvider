package com.app.namllahprovider.presentation

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.app.namllahprovider.databinding.ActivityMainBinding
import com.app.namllahprovider.presentation.base.ContextUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private val mainViewModel: MainViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        // get chosen language from shread preference
        val loggedUser = mainViewModel.getLoggedUser()
        val language = loggedUser?.language?.code
        val localeToSwitchTo = Locale(language ?: "en")
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    private var activityMainBinding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding?.root)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}