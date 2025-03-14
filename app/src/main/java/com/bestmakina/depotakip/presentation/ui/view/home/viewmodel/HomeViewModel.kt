package com.bestmakina.depotakip.presentation.ui.view.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestmakina.depotakip.data.local.SharedPreferencesHelper
import com.bestmakina.depotakip.domain.model.PreferencesKeys
import com.bestmakina.depotakip.presentation.ui.view.home.HomeAction
import com.bestmakina.depotakip.presentation.ui.view.home.HomeEffect
import com.bestmakina.depotakip.presentation.ui.view.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>(replay = 1)
    val effect = _effect.asSharedFlow()

    fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.LogOut -> logOut()
            is HomeAction.CustomNavigate -> navigateToPage(action.route)
        }
    }

    init {
        preparePage()
    }

    private fun preparePage() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                name = sharedPreferencesHelper.getData(PreferencesKeys.UserName),
                warehouseName = sharedPreferencesHelper.getData(PreferencesKeys.WareHouseName)
            )
        }
    }

    private fun navigateToPage(route: String) {
        _effect.tryEmit(HomeEffect.NavigateTo(route))
    }

    private fun logOut() {
        Log.d("HomeViewModel", "logOut: ")
        viewModelScope.launch {
            sharedPreferencesHelper.removeData(PreferencesKeys.UserId)
            sharedPreferencesHelper.removeData(PreferencesKeys.UserName)
        }
        _effect.tryEmit(HomeEffect.NavigateTo("login"))
        _effect.tryEmit(HomeEffect.ShowToast("Çıkış Yapıldı"))
    }
}