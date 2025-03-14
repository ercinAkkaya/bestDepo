package com.bestmakina.depotakip.domain.usecase

import com.bestmakina.depotakip.common.network.NetworkResult
import com.bestmakina.depotakip.data.model.request.personnel.WareHousePersonnel
import com.bestmakina.depotakip.domain.repository.WarehousePersonnelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonnelUseCase @Inject constructor(
    private val repository: WarehousePersonnelRepository
) {
    suspend operator fun invoke(): Flow<NetworkResult<WareHousePersonnel>> {
        return repository.getWareHousePersonnel()
    }
}