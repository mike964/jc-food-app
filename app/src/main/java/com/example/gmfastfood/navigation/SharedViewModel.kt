package com.example.gmfastfood.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedNavigationViewModel : ViewModel() {
    private val _sharedText = MutableStateFlow("")
    val sharedText: StateFlow<String> = _sharedText.asStateFlow()

    fun updateText(newText: String) {
        _sharedText.value = newText
    }
}

class SharedViewModel : ViewModel() {
    private val _sharedText = MutableStateFlow("")
    val sharedText: StateFlow<String> = _sharedText.asStateFlow()

    fun updateText(newText: String) {
        _sharedText.value = newText
    }
}