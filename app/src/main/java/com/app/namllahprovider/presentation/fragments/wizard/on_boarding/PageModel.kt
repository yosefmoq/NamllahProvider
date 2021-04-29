package com.app.namllahprovider.presentation.fragments.wizard.on_boarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class PageModel constructor(
    @DrawableRes image: Int,
    @StringRes title: Int,
    @StringRes description: Int,
    showNextButton: Boolean = false,
    showSkipButton: Boolean = true,
)