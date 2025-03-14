package com.bestmakina.depotakip.presentation.ui.view.TransferWithRecete

data class TransferWithReceteState(
    val personnelList: List<String> = emptyList(),
    val selectedPersonnel: String? = "Bir Değer Seçiniz",
)
