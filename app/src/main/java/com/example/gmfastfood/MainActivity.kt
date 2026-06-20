package com.example.gmfastfood

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gmfastfood.map.HtmlWebViewScreen
import com.example.gmfastfood.map.LeafletMapExample
import com.example.gmfastfood.map.LocalHtmlWebView
import com.example.gmfastfood.map.MapContainerDemo
import com.example.gmfastfood.ui.theme.GMFastFoodTheme
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import java.util.Locale

//class MainActivity : ComponentActivity() {
class MainActivity : AppCompatActivity() {  // Changed for app language switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Load the saved language on startup
        val languageManager = LanguageManager(this)
        val savedLanguage = languageManager.getLanguage()

        // 2. Apply it via AppCompatDelegate
        val localeList = LocaleListCompat.forLanguageTags(savedLanguage)
        AppCompatDelegate.setApplicationLocales(localeList)

        setContent {
            GMFastFoodTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                MainScreen()  // Main App
//                LeafletMapExample()
//                HtmlWebViewScreen()
//                LocalHtmlWebView( fileName = "leaflet_map.html", modifier = Modifier)
//                MapContainerDemo()
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    GMFastFoodTheme {
//        Greeting("Android")
//    }
//}

