package com.example.tp1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Event listener sur le btn commencer partie
        binding.btnStart.setOnClickListener {
            startGame()
        }
    }

    // Fermer le clavier quand on clic quelque part sur l'écran
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun startGame() {
        // Vérifier si un username est présent
        var username = binding.txtUsername.text.toString()
        if (username.isEmpty()) {
            val alertText = getString(R.string.no_username_provided)
            Toast.makeText(this, alertText, Toast.LENGTH_SHORT).show()
            return
        }


        // Sauvegarder le username
        val sharedPref =
            getSharedPreferences(getString(R.string.SHARED_PREF_PATH), Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            username = username.trim()
            putString(getString(R.string.shared_pref_USERNAME), username)
            apply()
        }

        // Cacher clavier
        hideKeyboard()

        // Commencer partie
        startActivity(Intent(this, QuizActivity::class.java))
    }

    /**
     * Cacher le clavier
     * https://dev.to/rohitjakhar/hide-keyboard-in-android-using-kotlin-in-20-second-18gp
     */
    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}


