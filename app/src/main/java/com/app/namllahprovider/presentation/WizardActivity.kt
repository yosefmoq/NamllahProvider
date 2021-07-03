package com.app.namllahprovider.presentation

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.preference.PreferenceManager
import com.app.namllahprovider.databinding.ActivityWizardBinding
import com.app.namllahprovider.domain.SharedValueFlags
import com.app.namllahprovider.presentation.base.ContextUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WizardActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {


    private var activityWizardBinding: ActivityWizardBinding? = null

    override fun attachBaseContext(newBase: Context) {
        val language = PreferenceManager.getDefaultSharedPreferences(newBase)
            .getString(SharedValueFlags.LANGUAGE.name, "en")
        val localeToSwitchTo = Locale(language ?: "en")
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWizardBinding = ActivityWizardBinding.inflate(layoutInflater)
        setContentView(activityWizardBinding?.root)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }
}