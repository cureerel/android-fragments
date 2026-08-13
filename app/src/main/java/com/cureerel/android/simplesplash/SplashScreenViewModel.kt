package com.cureerel.android.simplesplash

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel: ViewModel (){
    private val _isSplashScreenVisisble: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isSplashScreenVisible: StateFlow<Boolean> = _isSplashScreenVisisble.asStateFlow()

    init {

        viewModelScope.launch {
            delay(3000)
            _isSplashScreenVisisble.value = false
        }

    }
}