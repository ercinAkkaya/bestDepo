package com.bestmakina.depotakip

import TransferWithReceteView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bestmakina.depotakip.presentation.navigation.AppNavGraph
import com.bestmakina.depotakip.presentation.theme.DepoTakipTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            DepoTakipTheme {
                AppNavGraph(navController)
            }
        }
    }
}

