package com.bestmakina.depotakip.domain.repository

import com.bestmakina.depotakip.common.network.NetworkResult
import com.bestmakina.depotakip.data.model.request.personnel.BarcodeToNameData
import com.bestmakina.depotakip.data.model.request.personnel.WareHousePersonnel
import com.bestmakina.depotakip.data.model.response.device.TeslimAlanResponse
import kotlinx.coroutines.flow.Flow

interface WarehousePersonnelRepository {
    suspend fun getWareHousePersonnel(): Flow<NetworkResult<WareHousePersonnel>>
    suspend fun getBarcodeToName(barcode: String): Flow<NetworkResult<BarcodeToNameData>>
    suspend fun getTeslimAlanList(): Flow<NetworkResult<TeslimAlanResponse>>
}