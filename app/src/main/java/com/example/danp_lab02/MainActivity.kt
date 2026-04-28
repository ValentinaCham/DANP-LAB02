package com.example.danp_lab02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.danp_lab02.ui.HabitTrackerApp
import com.example.danp_lab02.ui.theme.DANPlab02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DANPlab02Theme {
                HabitTrackerApp()
            }
        }
    }
}
