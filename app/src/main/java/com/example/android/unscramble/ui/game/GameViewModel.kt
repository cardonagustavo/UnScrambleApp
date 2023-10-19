package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * ViewModel para la pantalla del juego. Gestiona la lógica del juego y la puntuación.
 */
class GameViewModel : ViewModel() {
    // Puntuación del jugador
    private var _score = 0
    val score: Int
        get() = _score

    // Contador de palabras actuales
    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    // Palabra actual barajada
    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    // Lista de palabras utilizadas en el juego
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    // Constructor
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    // Método llamado cuando se destruye el ViewModel
    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    /**
     * Obtiene la siguiente palabra para el juego y la baraja.
     */
    private fun getNextWord() {
        currentWord = allWordsList.random() // Selecciona una palabra al azar
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle() // Baraja las letras de la palabra

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord) // Palabra barajada
            ++_currentWordCount // Incrementa el contador de palabras actuales
            wordsList.add(currentWord) // Agrega la palabra a la lista de palabras utilizadas
        }
    }

    /**
     * Reinicia los datos del juego, incluida la puntuación y la lista de palabras.
     */
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }

    /**
     * Aumenta la puntuación del jugador.
     */
    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    /**
     * Comprueba si la palabra ingresada por el jugador es correcta y aumenta la puntuación si es correcta.
     */
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    /**
     * Obtiene la siguiente palabra para el juego y verifica si el juego ha alcanzado el número máximo de palabras.
     */
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    // Otras funciones y variables pueden agregarse según sea necesario para la lógica del juego.
}
