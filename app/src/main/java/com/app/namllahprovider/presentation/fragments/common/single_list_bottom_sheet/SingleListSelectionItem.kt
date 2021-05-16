package com.app.namllahprovider.presentation.fragments.common.single_list_bottom_sheet

import java.io.Serializable

data class SingleListSelectionItem(
    val selectionItem: (position: Int) -> Unit
) : Serializable
