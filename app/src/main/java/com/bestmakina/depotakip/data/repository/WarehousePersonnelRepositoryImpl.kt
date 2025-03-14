package com.bestmakina.depotakip.data.repository

import com.bestmakina.depotakip.common.network.NetworkResult
import com.bestmakina.depotakip.data.model.request.personnel.BarcodeToNameData
import com.bestmakina.depotakip.data.model.request.personnel.WareHousePersonnel
import com.bestmakina.depotakip.data.model.response.device.TeslimAlanResponse
import com.bestmakina.depotakip.data.remote.PersonnelApiService
import com.bestmakina.depotakip.domain.repository.WarehousePersonnelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WarehousePersonnelRepositoryImpl @Inject constructor(
    private val apiService: PersonnelApiService
) : WarehousePersonnelRepository{
    override suspend fun getWareHousePersonnel(): Flow<NetworkResult<WareHousePersonnel>> = flow{
        emit(NetworkResult.Loading())
        try {
            val response = apiService.getWarehousePersonnel()
            emit(NetworkResult.Success(response))
        }catch (e: Exception){
            emit(NetworkResult.Error(e.localizedMessage))
        }
    }

    override suspend fun getBarcodeToName(barcode: String): Flow<NetworkResult<BarcodeToNameData>> = flow{
        emit(NetworkResult.Loading())
        try {
            val response = apiService.getBarcodeToName(barcode)
            emit(NetworkResult.Success(response))
        }catch (e: Exception){
            emit(NetworkResult.Error(e.localizedMessage))
        }
    }

    override suspend fun getTeslimAlanList(): Flow<NetworkResult<TeslimAlanResponse>> = flow{
        emit(NetworkResult.Loading())
        try {
            val response = apiService.getTeslimAlanList()
            emit(NetworkResult.Success(response))
        }catch (e: Exception){
            emit(NetworkResult.Error(e.localizedMessage))
        }
    }
}