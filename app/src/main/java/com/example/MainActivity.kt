package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.KodiDatabase
import com.example.data.KodiRepository
import com.example.ui.KodiApp
import com.example.ui.KodiViewModel
import com.example.ui.KodiViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val database = KodiDatabase.getDatabase(applicationContext)
        val repository = KodiRepository(database.kodiDao())
        
        val viewModel: KodiViewModel = viewModel(
          factory = KodiViewModelFactory(repository)
        )
        
        KodiApp(viewModel = viewModel)
      }
    }
  }
}
