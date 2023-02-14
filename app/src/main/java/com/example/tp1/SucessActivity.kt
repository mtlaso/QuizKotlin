package com.example.tp1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tp1.databinding.ActivitySucessBinding

class SucessActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySucessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySucessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(getString(R.string.SHARED_PREF_PATH), Context.MODE_PRIVATE)

        // Afficher username
        val username = sharedPref.getString(getString(R.string.shared_pref_USERNAME), getString(R.string.default_username))
        binding.textCongratulation.text = getString(R.string.congrats_text, username)

        // Afficher score
        val score = sharedPref.getInt(getString(R.string.shared_pref_SCORE), 0)
        val nbQuestions = sharedPref.getInt(getString(R.string.shared_pref_TOTALQUESTIONS), 0)
        binding.textScore.text = getString(R.string.score_text, "$score/$nbQuestions")

        // Afficher le texte du score (3eme phrase)
        val scorestring = resources.getQuantityString(R.plurals.score2_text, score, score)
        binding.textScore2.text = scorestring

        /**
         * Clicklisteners
         */

        // Clicklisteners sur le bouton "rejouer"
        binding.btnPlayagain.setOnClickListener {
            // Commencer nouvelle partie
            startActivity(Intent(this, QuizActivity::class.java))
        }

        // Clicklisteners sur le bouton "partager"
        binding.btnShare.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND

                // Score à partager
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, score))

                // Titre
                putExtra(Intent.EXTRA_TITLE, getString(R.string.share_title_text))

                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }
    }
}