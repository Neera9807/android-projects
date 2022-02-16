package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int):ViewModel() {
    private val internalScore= MutableLiveData<Int>()
    private val internalEventPlayAgain = MutableLiveData<Boolean>()

    val score: LiveData<Int>get()= internalScore
    val eventPlayAgain: LiveData<Boolean> get() = internalEventPlayAgain

    init {
        internalScore.value=finalScore
    }

    fun onPlayAgain(){
        internalEventPlayAgain.value=true
    }

    fun onPlayAgainComplete(){
        internalEventPlayAgain.value=false
    }
}