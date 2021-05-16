package com.app.namllahprovider.presentation.fragments.common.radio_list_bottom_sheet

import java.io.Serializable

data class RadioListSelectionItem(
    val selectionItems: (positions: IntArray) -> Unit
) : Serializable
