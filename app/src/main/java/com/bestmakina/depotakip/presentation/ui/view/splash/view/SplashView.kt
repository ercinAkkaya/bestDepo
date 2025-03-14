package com.bestmakina.depotakip.presentation.ui.view.splash.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bestmakina.depotakip.R
import com.bestmakina.depotakip.presentation.navigation.Screens
import com.bestmakina.depotakip.presentation.ui.view.splash.SplashEffect
import com.bestmakina.depotakip.presentation.ui.view.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashView(navController: NavHostController, splashViewModel: SplashViewModel = hiltViewModel()) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        splashViewModel.effect.collect { effect ->
            when (effect) {
                is SplashEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is SplashEffect.NavigateTo -> {
                    delay(1000)
                    navController.navigate(effect.destination) {
                        popUpTo(Screens.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(28.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.bst_logo_small),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
