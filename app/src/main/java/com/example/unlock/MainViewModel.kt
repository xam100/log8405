package com.example.unlock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * MainViewModel class used to contain LiveData values
 * Used in other classes that access and edit those values
 * Also used in Data binding in various xml layouts
 */
class MainViewModel: ViewModel() {
    val moves: LiveData<Int>
        get() = GridManagerObject.moves
    val fresh: LiveData<Boolean>
        get() = GridManagerObject.fresh

    var win: MutableLiveData<Boolean> = GridManagerObject.win
}