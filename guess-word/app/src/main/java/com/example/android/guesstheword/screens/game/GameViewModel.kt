package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel: ViewModel() {
    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 20000L

        private const val COUNTDOWN_PANIC_SECONDS = 2000L
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    private val internalWord = MutableLiveData<String>()
    private val internalScore = MutableLiveData<Int>()
    private val internalEventGameFinish = MutableLiveData<Boolean>()
    private var internalTimer: CountDownTimer
    private val internalCurrentTime=MutableLiveData<Long>()
    private val internalEventBuzz = MutableLiveData<BuzzType>()
    private val timer: LiveData<Long>get() = internalCurrentTime

    val score: LiveData<Int>get()=internalScore
    val word: LiveData<String>get() = internalWord
    val eventGameFinish: LiveData<Boolean>get() = internalEventGameFinish
    val timeString = Transformations.map(timer) { newTime ->
        DateUtils.formatElapsedTime(newTime)
    }
    val eventBuzz: LiveData<BuzzType>get()=internalEventBuzz

    private lateinit var wordList: MutableList<String>
    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //gameFinished()
            //internalEventGameFinish.value=true
            resetList()
        } //else {
            internalWord.value = wordList.removeAt(0)
        //}
        //updateWordText()
        //updateScoreText()
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        internalScore.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        internalScore.value = score.value?.plus(1)
        internalEventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishCompleted(){
        internalEventGameFinish.value=false
    }

    fun onBuzzComplete(){
        internalEventBuzz.value = BuzzType.NO_BUZZ
    }

    init {
        //Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
        internalScore.value=0
        internalEventGameFinish.value=false
        internalTimer= object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                internalCurrentTime.value=millisUntilFinished/ ONE_SECOND
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    internalEventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                internalEventGameFinish.value=true
                internalCurrentTime.value= DONE
                internalEventBuzz.value = BuzzType.GAME_OVER
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        //Log.i("GameViewModel",  "GameViewModel destroyed!")
        internalTimer.cancel()
    }
}