package com.example.unlock

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val moves: LiveData<Int>
        get() = GridManagerObject.test
}