package com.example.tp1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp1.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Click listeners
         */

        // Clicklistener sur le bouton "confirmer"
        binding.btnConfirm.setOnClickListener {
            // Changer username
            changeUsername()
        }

        // Clicklistener sur le bouton "retour" sur la top bar
        binding.topAppBar.setOnClickListener{
            // Retour à l'écran précédent
            startActivity(Intent(this, QuizActivity::class.java))
        }
    }

    /**
     * Changer le username de l'utilisateur
     */
    private fun changeUsername() {
        // Valider username
        var username = binding.txtNewUsername.text.toString()
        if (username.isEmpty()) {
            val alertText = getString(R.string.no_username_provided)
            Toast.makeText(this, alertText, Toast.LENGTH_SHORT).show()
            return
        }

        // Sauvegarder le nouveau username
        val sharedPref =
            getSharedPreferences(getString(R.string.SHARED_PREF_PATH), Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            username = username.trim()
            putString(getString(R.string.shared_pref_USERNAME), username)
            apply()
        }

        // Retour à l'écran précédent
        startActivity(Intent(this, QuizActivity::class.java))
    }
}