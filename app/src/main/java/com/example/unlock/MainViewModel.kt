package com.example.unlock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val moves: LiveData<Int>
        get() = GridManagerObject.moves
    val fresh: LiveData<Boolean>
        get() = GridManagerObject.fresh

    var win: MutableLiveData<Boolean> = GridManagerObject.win
}