package com.bestmakina.depotakip.presentation.ui.view.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestmakina.depotakip.common.network.NetworkResult
import com.bestmakina.depotakip.data.local.SharedPreferencesHelper
import com.bestmakina.depotakip.domain.model.PreferencesKeys
import com.bestmakina.depotakip.domain.usecase.GetNameByBarcodeUseCase
import com.bestmakina.depotakip.domain.usecase.GetPersonnelUseCase
import com.bestmakina.depotakip.presentation.ui.view.login.LoginAction
import com.bestmakina.depotakip.presentation.ui.view.login.LoginEffect
import com.bestmakina.depotakip.presentation.ui.view.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getPersonnelUseCase: GetPersonnelUseCase,
    private val getNameToBarcodeUseCase: GetNameByBarcodeUseCase,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.SetBarcodeResult -> getNameByBarcode(action.barcodeData)
        }
    }

    init {
        sharedPreferencesHelper.saveData(PreferencesKeys.WareHouseName, "ÜstDepo9")
        preparePage()
    }

    private fun preparePage() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            fetchUsers()
            delay(500)
            getExistingUser()
        }
    }
    private suspend fun fetchUsers() {
        getPersonnelUseCase().collectLatest { result ->
            when (result) {
                is NetworkResult.Loading -> Log.d("LoginViewModel", "fetchUsers: Loading")
                is NetworkResult.Success -> {
                    val personnelList = result.data?.sorumluPersonel ?: emptyList()
                    _state.value = _state.value.copy(personnelList = personnelList)
                    Log.d("LoginViewModel", "fetchUsers: Success, personnelList size: ${personnelList.size}")
                }
                is NetworkResult.Error -> Log.d("LoginViewModel", "fetchUsers: Error, message: ${result.message}")
            }
        }
    }


    private fun getExistingUser() {
        viewModelScope.launch {
            Log.d("LoginViewModel", "getExistingUser")
            fetchUsers()
            val userId = sharedPreferencesHelper.getData(PreferencesKeys.UserId)
            if (userId.isNotEmpty()) {
                Log.d("LoginViewModel", "getExistingUser: $userId")
                getNameByBarcode(userId)
            } else {
                _state.value = _state.value.copy(isLoading = false)
                _effect.emit(LoginEffect.ShowToast("Lütfen Giriş Yapmak İçin Barkod Okutunuz"))
            }
        }
    }

    private fun getNameByBarcode(barcode: String) {
        viewModelScope.launch {
            getNameToBarcodeUseCase(barcode).collectLatest { result ->
                when (result) {
                    is NetworkResult.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is NetworkResult.Success -> {
                        result.data?.let {
                            Log.d("LoginViewModel", "getNameByBarcode: Success, personnelName: ${it.personnelAdi}")
                            checkPersonnel(it.personnelAdi, barcode)
                            _state.value = _state.value.copy(isLoading = false)
                        }
                    }
                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(isLoading = true)
                        Log.d("LoginViewModel", "getNameByBarcode: Error, message: ${result.message}")
                        _effect.emit(LoginEffect.ShowToast("Barkod okuma hatası: ${result.message}"))
                    }
                }
            }
        }
    }


    private fun checkPersonnel(personnelName: String, barcodeData: String) {
        val exist = _state.value.personnelList.any { it.Adi == personnelName }
        Log.d("LoginViewModel", "checkPersonnel: $exist, $personnelName, $barcodeData")
        viewModelScope.launch {
            if (exist) {
                sharedPreferencesHelper.saveData(PreferencesKeys.UserName, personnelName)
                sharedPreferencesHelper.saveData(PreferencesKeys.UserId, barcodeData)
                _effect.emit(LoginEffect.NavigateTo("home"))
            } else {
                _state.value = _state.value.copy(isLoading = false)
                _effect.emit(LoginEffect.ShowToast("Personel bulunamadı"))
            }
        }
    }
}