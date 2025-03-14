package com.bestmakina.depotakip.presentation.ui.view.TransferWithRecete

sealed class TransferWithReceteAction {
    data class SelectItem(val selectedType: String, val selectedValue: String) : TransferWithReceteAction()
}