package com.example.tp1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tp1.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private var SAVED_STATE_USERSCORE = "SAVED_STATE_USERSCORE"
    private var SAVED_STATE_CURRENTQUESTIONINDEX = "SAVED_STATE_CURRENTQUESTIONINDEX"

    /**
     * Score de l'utilisateur
     */
    private var userscore = 0

    /**
     * Index de la réponse séléctionnée
     */
    private var selectedSolutionIndex: Int? = null

    /**
     * Index de la question actuelle
     */
    private var currentQuestionIndex = 0

    /**
     * Liste des questions
     */
    private val questionsList =
        listOf(
            Question(
                "Quelle bierre iconique Homer Simpson boit ?",
                listOf("Duff", "Heineken", "Molson", "Budlight"),
                0,
                R.drawable.beer
            ),
            Question(
                "Que signifie l’expression :  « Tenter le coup » ?",
                listOf("Abandonner", "Boire un verre", "Essayer", "Aider une personne"),
                2,
                R.drawable.expression
            ),
            Question(
                "Quelle est la capitale de la Suisse ? ",
                listOf("Berne", "Genève", "Vaduz", "Zurich"),
                0,
                R.drawable.suisse2
            ),
            Question(
                "Dans quel pays s'est passée la coupe du monde de football de 2014 ? ",
                listOf("France", "Argentine", "Afrique du Sud", "Brésil"),
                3,
                R.drawable.cup
            ),
            Question(
                "Quelle est la longueur d'une piscine olympique ?",
                listOf("25 mètres", "50 mètres", "45 mètres", "40 mètres"),
                1,
                R.drawable.pool
            )
        )

    // Sauvegarder score et l'index de la solution (en cas de rotation)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Sauvegarder le score et l'index de la question à la quelle nous sommes rendu
        outState.putInt(SAVED_STATE_USERSCORE, userscore)
        outState.putInt(SAVED_STATE_CURRENTQUESTIONINDEX, currentQuestionIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser la barre de progression
        binding.progressIndicator.max = questionsList.size

        // Initiliaser score et index questions (si sauvegardé dans onSaveInstanceState)
        loadSavedState(savedInstanceState)

        /**
         * Click listeners
         */

        // Clicklistener sur la top bar
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            // Clic sur le bouton "settings"
            when (menuItem.itemId) {
                R.id.top_bar_menu_settings -> {
                    // Ouvrir vue paramètres
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Clicklistener sur la 1ere réponse
        binding.question0.setOnClickListener {
            // Changer couleur bouton
            changeBgColor(it)

            // Retier couleurs des autres boutons
            val btnsRemoveBg = listOf(binding.question1, binding.question2, binding.question3)
            removeBtnBgColor(btnsRemoveBg)

            // Changer index de la solution séléctionnée
            selectedSolutionIndex = 0
        }

        // Clicklistener sur la 2eme réponse
        binding.question1.setOnClickListener {
            // Changer couleur bouton
            changeBgColor(it)

            // Retier couleurs des autres boutons
            val btnsRemoveBg = listOf(binding.question0, binding.question2, binding.question3)
            removeBtnBgColor(btnsRemoveBg)

            // Changer index de la solution séléctionnée
            selectedSolutionIndex = 1
        }

        // Clicklistener sur la 3eme réponse
        binding.question2.setOnClickListener {
            // Changer couleur bouton
            changeBgColor(it)

            // Retier couleurs des autres boutons
            val btnsRemoveBg = listOf(binding.question0, binding.question1, binding.question3)
            removeBtnBgColor(btnsRemoveBg)

            // Changer index de la solution séléctionnée
            selectedSolutionIndex = 2
        }

        // Clicklistener sur la 4eme réponse
        binding.question3.setOnClickListener {
            // Changer couleur bouton
            changeBgColor(it)

            // Retier couleurs des autres boutons
            val btnsRemoveBg = listOf(binding.question0, binding.question1, binding.question2)
            removeBtnBgColor(btnsRemoveBg)

            // Changer index de la solution séléctionnée
            selectedSolutionIndex = 3
        }

        // Clicklistener sur le bouton "confirmer"
        binding.btnConfirmQuestion.setOnClickListener {
            verifyAnswer()
        }

        // Clicklistener sur le bouton "Continuer" (prochaine question)
        binding.btnNextQuestion.setOnClickListener {
            // Afficher prochaine question
            showQuestion(currentQuestionIndex)
        }


        // Afficher la première question (début)
        showQuestion(currentQuestionIndex)
    }

    /**
     * Charger les informations sauvegardées par la fonction "onSaveInstanceState"
     * @param savedInstanceState Bundle contentant les informations
     */
    private fun loadSavedState(savedInstanceState: Bundle?) {
        val savedUserscore = savedInstanceState?.getInt(SAVED_STATE_USERSCORE)
        val savedQuestionIndex = savedInstanceState?.getInt(SAVED_STATE_CURRENTQUESTIONINDEX)

        if (savedUserscore != null) {
            userscore = savedUserscore
        }

        if (savedQuestionIndex != null) {
            currentQuestionIndex = savedQuestionIndex
        }
    }

    /**
     * Afficher la mauvaise réponse en rouge
     * @param index de la solution
     */
    private fun showWrongAnswer(index: Int) {
        when (index) {
            0 -> {
                changeBgColor(binding.question0, R.color.custom_color_red)
            }

            1 -> {
                changeBgColor(binding.question1, R.color.custom_color_red)
            }
            2 -> {
                changeBgColor(binding.question2, R.color.custom_color_red)
            }
            3 -> {
                changeBgColor(binding.question3, R.color.custom_color_red)
            }
        }
    }

    /**
     * Afficher la bonne réponse en vert
     * @param index Index de la solution
     */
    private fun showCorrectAnswer(index: Int) {
        when (index) {
            0 -> {
                changeBgColor(binding.question0, R.color.custom_color_green)
            }

            1 -> {
                changeBgColor(binding.question1, R.color.custom_color_green)
            }
            2 -> {
                changeBgColor(binding.question2, R.color.custom_color_green)
            }
            3 -> {
                changeBgColor(binding.question3, R.color.custom_color_green)
            }
        }
    }


    /**
     * Affiche la question, et ces réponses selon l'index
     * @param index Index de la question à afficher
     */
    private fun showQuestion(index: Int) {
        // Vérifier si le quiz est fini
        if (index >= questionsList.size) {
            // Sauvegarder score
            saveInfoToSharedpref()

            // Afficher écran de fin
            startActivity(Intent(this, SucessActivity::class.java))
            return
        }

        val currentQuestion = questionsList[index]

        // Afficher la question
        binding.question.text = currentQuestion.question

        // Afficher les réponses
        binding.question0.text = currentQuestion.answers[0]
        binding.question1.text = currentQuestion.answers[1]
        binding.question2.text = currentQuestion.answers[2]
        binding.question3.text = currentQuestion.answers[3]

        // Mettre a jour la barre de progression
        binding.progressIndicator.progress = index + 1

        // Afficher le numéro de la question actuelle
        binding.progressionText.text = "${index + 1}/${questionsList.size}"

        // Changer image
        binding.quizImage.setImageResource(currentQuestion.image)

        // Afficher le bouton prochaine "confirmer"
        // Cacher bouton "prochaine question"
        binding.btnConfirmQuestion.visibility = View.VISIBLE
        binding.btnNextQuestion.visibility = View.INVISIBLE

        // Retirer les fonds sur les boutons
        val btnsRemoveBg =
            listOf(binding.question0, binding.question1, binding.question2, binding.question3)
        removeBtnBgColor(btnsRemoveBg)
    }


    /**
     * Vérifie la réponse d'une question
     */
    private fun verifyAnswer() {
        // Vérifier qu'une solution a été séléctionnée
        if (selectedSolutionIndex == null) {
            Toast.makeText(this, getString(R.string.no_answer_selected), Toast.LENGTH_SHORT).show()
            return
        }

        val currentQuestion = questionsList[currentQuestionIndex]

        // Bonne solution à la question
        val correctIndex = currentQuestion.solutionIndex
        val solution = currentQuestion.answers[correctIndex].lowercase().trim()

        // Solution séléctionnée par l'utlisateur
        val selectedSolution = currentQuestion.answers[selectedSolutionIndex!!].lowercase().trim()

        // Vérifier si la réponse est bonne
        val isValid = solution == selectedSolution

        // Afficher la bonne réponse en vert
        showCorrectAnswer(correctIndex)

        // Si invalide, afficher la mauvaise réponse en rouge, la bonne en vert
        if (!isValid) {
            showWrongAnswer(selectedSolutionIndex!!)
        } else {
            // incrémenter score
            userscore++
        }

        // Afficher le bouton "prochaine question"
        // Cacher bouton "confirmer"
        binding.btnConfirmQuestion.visibility = View.INVISIBLE
        binding.btnNextQuestion.visibility = View.VISIBLE

        // Changer l'index de la question vers la prochaine
        currentQuestionIndex++

        // Mettre l'index de la réponse séléctionnée à null
        selectedSolutionIndex = null
    }

    /**
     * Retirer le fond de couleur des boutons
     * @param buttons Liste des boutons
     */
    private fun removeBtnBgColor(buttons: List<Button>) {
        for (btn in buttons) {
            btn.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    com.google.android.material.R.color.mtrl_btn_transparent_bg_color
                )
            )

        }

    }

    /**
     * Changer la couleur de fond d'une vue
     * @param it La vue à changer
     * @param customColor Couleur à mettre (optionnel) (R.color.xxx)currentQuestionIndex
     */
    private fun changeBgColor(it: View, customColor: Int = R.color.purple_500) {
        it.setBackgroundColor(ContextCompat.getColor(this, customColor))
    }

    /**
     * Sauvegarder des informations (score, liste des questions) dans les shared prefrences
     */
    private fun saveInfoToSharedpref() {
        val sharedPref =
            getSharedPreferences(getString(R.string.SHARED_PREF_PATH), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt(getString(R.string.shared_pref_SCORE), userscore)
            putInt(getString(R.string.shared_pref_TOTALQUESTIONS), questionsList.size)
            apply()
        }
    }
}