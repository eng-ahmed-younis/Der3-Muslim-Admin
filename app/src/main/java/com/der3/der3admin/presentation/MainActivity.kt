package com.der3.der3admin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.der3.der3admin.presentation.navigation.MainNavHost
import com.der3.der3admin.presentation.screens.home.HomeScreen
import com.der3.der3admin.presentation.screens.home.mvi.HomeState
import com.der3.ui.themes.Der3AdminTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Der3AdminTheme {
                val navController = rememberNavController()
                MainNavHost(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Der3AdminTheme {
        // Since MainNavHost requires Hilt, we preview the default HomeScreen UI here
        HomeScreen(
            state = HomeState(),
            onIntent = {}
        )
    }
}
