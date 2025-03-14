package com.bestmakina.depotakip.presentation.ui.view.login

sealed class LoginAction {
    data class SetBarcodeResult(val barcodeData: String) : LoginAction()
}