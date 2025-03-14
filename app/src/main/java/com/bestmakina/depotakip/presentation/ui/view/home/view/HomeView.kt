package com.bestmakina.depotakip.presentation.ui.view.home.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bestmakina.depotakip.R
import com.bestmakina.depotakip.component.card.OperationCard
import com.bestmakina.depotakip.presentation.navigation.Screens
import com.bestmakina.depotakip.presentation.ui.view.home.HomeAction
import com.bestmakina.depotakip.presentation.ui.view.home.HomeEffect
import com.bestmakina.depotakip.presentation.ui.view.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeView(navController: NavHostController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val operations = listOf(
        Triple("Üretim Transfer", R.drawable.setting) { /* Üretim Transfer OnClick */ },
        Triple("Üretim İade", R.drawable.setting) { /* Üretim İade OnClick */ },
        Triple("Servis Transfer", R.drawable.production) { /* Servis Transfer OnClick */ },
        Triple("Servis İade", R.drawable.production) { /* Servis İade OnClick */ },
        Triple("Envanter", R.drawable.inventory) { /* Envanter OnClick */ },
        Triple("Barkod Yazdır", R.drawable.printer) { /* Barkod Yazdır OnClick */ },
        Triple("Raf Düzenle", R.drawable.shelves) { /* Raf Düzenle OnClick */ },
        Triple(
            "Reçeteden Transfer",
            R.drawable.shelves
        ) { homeViewModel.handleAction(HomeAction.CustomNavigate("transferWithRecete")) },
        Triple(
            "Sayım",
            R.drawable.counter
        ) { homeViewModel.handleAction(HomeAction.CustomNavigate("stockTaking")) }
    )

    val state = homeViewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeViewModel.effect.collect { homeEffect ->
            when (homeEffect) {
                is HomeEffect.NavigateTo -> {
                    Log.d("HomeView", "HomeEffect.NavigateTo: ${homeEffect.route}")
                    navController.navigate(homeEffect.route)
                }

                is HomeEffect.ShowToast -> Toast.makeText(
                    context,
                    homeEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-25).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1.6f))
            Image(
                modifier = Modifier.size(170.dp),
                painter = painterResource(R.drawable.bst_logo_small),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        homeViewModel.handleAction(HomeAction.LogOut)
                    },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .size(40.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .padding(6.dp),
                        painter = painterResource(R.drawable.logout),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Çıkış Yap",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 110.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        state.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
                    )
                    Text(
                        state.warehouseName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(operations) { operation ->
                        OperationCard(
                            onClick = operation.third,  // Özel OnClick fonksiyonu çağırılıyor
                            text = operation.first,
                            icon = painterResource(id = operation.second)
                        )
                    }
                    item {
                        OperationCard(
                            onClick = { /* Sanal Depo OnClick */ },
                            text = "Sanal Depo",
                            icon = painterResource(id = R.drawable.parcel),
                        )
                    }
                }
            }
        }
    }
}