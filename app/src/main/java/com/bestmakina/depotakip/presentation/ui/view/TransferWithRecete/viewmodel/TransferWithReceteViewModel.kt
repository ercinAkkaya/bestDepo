package com.bestmakina.depotakip.presentation.ui.view.TransferWithRecete.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestmakina.depotakip.common.network.NetworkResult
import com.bestmakina.depotakip.domain.usecase.GetTeslimAlanUseCase
import com.bestmakina.depotakip.presentation.ui.view.TransferWithRecete.TransferWithReceteAction
import com.bestmakina.depotakip.presentation.ui.view.TransferWithRecete.TransferWithReceteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferWithReceteViewModel @Inject constructor(
    private val getTeslimAlanUseCase: GetTeslimAlanUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(TransferWithReceteState())
    val state = _state.asStateFlow()

    fun handleAction(action: TransferWithReceteAction) {
        when (action) {
            is TransferWithReceteAction.SelectItem -> handleSelect(action.selectedType, action.selectedValue)
        }
    }

    init {
        preparePage()
    }

    private fun preparePage(){
        viewModelScope.launch {
            getTeslimAlanUseCase().collectLatest { result ->
                when (result) {
                    is NetworkResult.Error -> Log.d("TransferWithReceteViewModel", "fetchUsers: Error")
                    is NetworkResult.Loading -> Log.d("TransferWithReceteViewModel", "fetchUsers: Loading")
                    is NetworkResult.Success -> {
                        val teslimAlanList = result.data?.Liste?.map {
                            it.TeslimAlan
                        } ?: emptyList()
                        _state.value = _state.value.copy(personnelList = teslimAlanList)
                    }
                }

            }
        }
    }

    private fun handleSelect( selectedType: String, selectedValue: String) {
        when (selectedType) {
            "selectedPersonnel" -> _state.value = _state.value.copy(selectedPersonnel = selectedValue)
        }
    }


}