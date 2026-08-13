package com.cureerel.android.simplesplash

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.cureerel.android.simplesplash.jetpackcompose.HomeScreen
import com.cureerel.android.simplesplash.ui.theme.SimplesplashTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // splashScreenViewModel
        val splashScreenViewModel : SplashScreenViewModel by lazy {
            ViewModelProvider(this@MainActivity).get(SplashScreenViewModel::class.java)
        }

        // splash screen state
        installSplashScreen().apply {
            setKeepOnScreenCondition { splashScreenViewModel.isSplashScreenVisible.value }
            setOnExitAnimationListener { splash ->
                val rotationAnimator = ValueAnimator.ofFloat(0f, 90f)
                rotationAnimator.duration = 500
                rotationAnimator.addUpdateListener {
                    splash.iconView.rotation = it.animatedValue as Float
                }
                rotationAnimator.doOnEnd {
                    splash.remove()
                }
                rotationAnimator.start()
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimplesplashTheme {
                // link a page (import with Package) : import com.cureerel.android.simplesplash.jetpackcompose.HomeScreen
                    HomeScreen()
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimplesplashTheme {
        Greeting("Android")
    }
}