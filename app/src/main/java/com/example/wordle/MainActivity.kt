package com.example.wordle

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var wordToGuess = ""
    private var guessCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val guessInput = findViewById<EditText>(R.id.guessInput)
        val submitBtn = findViewById<Button>(R.id.submitBtn)
        val resetBtn = findViewById<Button>(R.id.resetBtn)
        val targetWordTv = findViewById<TextView>(R.id.targetWord)

        // Initialize game
        resetGame()

        submitBtn.setOnClickListener {
            val guess = guessInput.text.toString().uppercase()

            if (guess.length != 4) {
                Toast.makeText(this, "Please enter a 4-letter word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guessCount++
            val result = checkGuess(guess)

            // Update the UI for the current guess immediately
            displayGuess(guessCount, guess, result)

            guessInput.text.clear()

            if (guessCount == 3) {
                submitBtn.visibility = View.GONE
                resetBtn.visibility = View.VISIBLE
                targetWordTv.visibility = View.VISIBLE
                guessInput.isEnabled = false
                closeKeyboard()
                Toast.makeText(this, "You've exceeded your number of guesses", Toast.LENGTH_LONG).show()
            }
        }

        resetBtn.setOnClickListener {
            resetGame()
        }
    }

    private fun resetGame() {
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        guessCount = 0

        val guessInput = findViewById<EditText>(R.id.guessInput)
        val submitBtn = findViewById<Button>(R.id.submitBtn)
        val resetBtn = findViewById<Button>(R.id.resetBtn)
        val targetWordTv = findViewById<TextView>(R.id.targetWord)

        targetWordTv.text = wordToGuess
        targetWordTv.visibility = View.INVISIBLE
        
        guessInput.isEnabled = true
        guessInput.text.clear()
        
        submitBtn.visibility = View.VISIBLE
        submitBtn.isEnabled = true
        resetBtn.visibility = View.GONE

        // Reset visibility of guess displays
        findViewById<TextView>(R.id.guess1Label).visibility = View.VISIBLE
        findViewById<TextView>(R.id.guess1Value).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.guess1CheckLabel).visibility = View.VISIBLE
        findViewById<TextView>(R.id.guess1CheckValue).visibility = View.INVISIBLE

        findViewById<TextView>(R.id.guess2Label).visibility = View.VISIBLE
        findViewById<TextView>(R.id.guess2Value).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.guess2CheckLabel).visibility = View.VISIBLE
        findViewById<TextView>(R.id.guess2CheckValue).visibility = View.INVISIBLE

        findViewById<TextView>(R.id.guess3Label).visibility = View.VISIBLE
        findViewById<TextView>(R.id.guess3Value).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.guess3CheckLabel).visibility = View.VISIBLE
        findViewById<TextView>(R.id.guess3CheckValue).visibility = View.INVISIBLE
    }

    private fun displayGuess(count: Int, guess: String, result: String) {
        when (count) {
            1 -> {
                findViewById<TextView>(R.id.guess1Value).apply { text = guess; visibility = View.VISIBLE }
                findViewById<TextView>(R.id.guess1CheckValue).apply { text = result; visibility = View.VISIBLE }
            }
            2 -> {
                findViewById<TextView>(R.id.guess2Value).apply { text = guess; visibility = View.VISIBLE }
                findViewById<TextView>(R.id.guess2CheckValue).apply { text = result; visibility = View.VISIBLE }
            }
            3 -> {
                findViewById<TextView>(R.id.guess3Value).apply { text = guess; visibility = View.VISIBLE }
                findViewById<TextView>(R.id.guess3CheckValue).apply { text = result; visibility = View.VISIBLE }
            }
        }
    }

    private fun checkGuess(guess: String): String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            } else if (guess[i] in wordToGuess) {
                result += "+"
            } else {
                result += "X"
            }
        }
        return result
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
