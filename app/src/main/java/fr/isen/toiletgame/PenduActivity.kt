package fr.isen.toiletgame

import android.app.AlertDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pendu.*
import java.io.BufferedReader
import java.io.CharArrayWriter
import java.io.IOException
import java.io.InputStreamReader

class PenduActivity : AppCompatActivity(), View.OnClickListener {

    private var win: Boolean = false
    private var error: Int = 0
    private var found: Int = 0
    private var mot: String = ""
    private var listOfLetters = CharArrayWriter()
    private var listofWords = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu)

        initGame()
        regles.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@PenduActivity)
            alertDialog.setTitle("Règles du jeu")
            alertDialog.setMessage("L'objectif du jeu est de découvrir un mot en devinant les lettres le composant. À chaque tour, le joueur choisit une lettre de l'alphabet qu'il estime pouvant faire partie du mot à deviner. Si le mot contient cette lettre, celle-ci sera montrée et placée à sa/ses position(s) dans la composition du mot. Sinon, un croquis représentant un corps humain sera peu à peu formé. Lorsque les 6 parties de ce croquis sont terminées, le joueur a perdu.")
            alertDialog.setNeutralButton("Ok"){_,_ -> }
            alertDialog.create().show()
            regles.hideKeyboard()
        }
        btn_send.setOnClickListener(this)

    }

    private fun initGame() {
        mot = generateMot()
        win = false
        error = 0
        found = 0
        tv_lettres_tapees.text = ""
        iv_pendu.setBackgroundResource(R.drawable.first)
        listOfLetters = CharArrayWriter()

        word_container.removeAllViews()

        for (lettre in mot) {
            val oneLetter = layoutInflater.inflate(R.layout.textview, null) as TextView?
            word_container.addView(oneLetter)
        }

    }

    override fun onClick(v: View) {
        var lettreFromInput: String = et_letter.text.toString().toUpperCase()
        et_letter.text.clear()

        if (lettreFromInput.isNotEmpty() && (lettreFromInput.single() in 'a'..'z' || lettreFromInput.single() in 'A'..'Z')) {
            if (!listOfLetters.toString().contains(lettreFromInput.single())) {
                listOfLetters.append(lettreFromInput.single())
                verifieLettreDansMot(lettreFromInput.single(), mot)
            }
            else {
                Toast.makeText(this, "Vous avez déjà entré cette lettre", Toast.LENGTH_SHORT).show()
            }

            // Partie gagné
            if (found == mot.length) {
                win = true
                creerDialog(win)
            }

            // La lettre n'est pas dans le mot
            if (!mot.contains(lettreFromInput)) {
                error++
            }
            setImage(error)
            if (error == 6) {
                win = false
                creerDialog(win)
            }

            afficheLettres(listOfLetters)

        }
        else {
            Toast.makeText(this,"Entrez une lettre de l'alphabet.", Toast.LENGTH_SHORT).show()
        }
    }

    fun verifieLettreDansMot(lettre: Char, mot: String) {
        var i = 0
        for (letter in mot) {
            if (letter == lettre) {
                var tv = word_container.getChildAt(i) as TextView?
                tv?.text = letter.toString()
                found++
            }
            i++
        }
    }

    fun afficheLettres(listOfLetters: CharArrayWriter) {
        var chaine = ""
        for (lettre in listOfLetters.toString()) {
            chaine += lettre.toString() + "\n"
        }
        if (chaine.isNotEmpty()) {
            tv_lettres_tapees.text = chaine
        }
    }

    fun setImage(error: Int) {
        when(error) {
            1 ->  iv_pendu.setBackgroundResource(R.drawable.second)
            2 ->  iv_pendu.setBackgroundResource(R.drawable.third)
            3 ->  iv_pendu.setBackgroundResource(R.drawable.fourth)
            4 ->  iv_pendu.setBackgroundResource(R.drawable.fifth)
            5 ->  iv_pendu.setBackgroundResource(R.drawable.sixth)
            6 ->  iv_pendu.setBackgroundResource(R.drawable.seventh)
        }
    }

    fun creerDialog(win: Boolean) {
        val builder = AlertDialog.Builder(this@PenduActivity)
        builder.setTitle("Vous avez gagné !")

        if (!win) {
            builder.setTitle("Vous avez perdu !")
            builder.setMessage("Le mot à trouver était : " + mot)
        }
        builder.setPositiveButton(resources.getString(R.string.rejouer)) { _,_ ->
            initGame()
        }

        builder.setNeutralButton("Menu"){_,_ ->
            finish()
        }

        builder.create().show()
        tv_lettres_tapees.hideKeyboard()
    }

    fun getListeMots(): ArrayList<String> {

        try {
            val buffer = BufferedReader(InputStreamReader(assets.open("pendu_liste.txt")))
            val iterator = buffer.lineSequence().iterator()
            while (iterator.hasNext()) {
                val line = iterator.next()
                listofWords.add(line)
            }
            buffer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listofWords
    }

    fun generateMot(): String {
        listofWords = getListeMots()
        val random = Math.floor(Math.random() * listofWords.size)
        val mot = listofWords.get(random.toInt()).trim()
        return mot
    }

    override fun onBackPressed() {
        finish()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}
