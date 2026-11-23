package com.aza.apkkonseling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aza.apkkonseling.navigation.BkAppNavigation
import com.aza.apkkonseling.theme.BkCounselingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BkCounselingTheme {
                BkAppNavigation()
            }
        }
    }
}
