package com.app.namllahprovider.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.app.namllahprovider.databinding.ActivityWizardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WizardActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private var activityWizardBinding: ActivityWizardBinding? = null

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