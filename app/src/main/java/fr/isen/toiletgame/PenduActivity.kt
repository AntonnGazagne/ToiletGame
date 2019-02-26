package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pendu.*
import java.io.CharArrayWriter

abstract class PenduActivity : AppCompatActivity() {

    private var win: Boolean = false
    private var error: Int = 0
    private var found: Int = 0
    private var mot: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu)

        initGame()
        btn_send.setOnClickListener { envoiLettre() }

    }

    private fun initGame() {
        mot = "ORDINATEUR"
        tv_lettres_tapees.text = ""
        iv_pendu.setBackgroundResource(R.drawable.first)

        word_container.removeAllViews()

        for (lettre in mot) {
            val oneLetter = layoutInflater.inflate(R.layout.textview, null) as TextView?
            word_container.addView(oneLetter)
        }

    }

    fun envoiLettre() {
        var lettreFromInput: String = et_letter.text.toString().toUpperCase()
        et_letter.text.clear()

        if (lettreFromInput.isNotEmpty()) {
            var listOfLetters = CharArrayWriter()
            if (!listOfLetters.toCharArray().contains(lettreFromInput.get(0))) {
                listOfLetters.append(lettreFromInput)
                verifieLettreDansMot(lettreFromInput, mot)
            }
            else {
                Toast.makeText(this, "Vous avez déjà entré cette lettre", Toast.LENGTH_SHORT).show()
            }

            // Partie gagné
            if (found == mot.length) {
                win = true
                Toast.makeText(this, "Victoire !", Toast.LENGTH_LONG).show()
            }

            // La lettre n'est pas dans le mot
            if (!mot.contains(lettreFromInput)) {
                error++
            }
            if (error == 6) {
                win = false
                Toast.makeText(this, "Perdu !", Toast.LENGTH_LONG).show()
            }

            //Affichage des lettres tapées
            afficheLettres(listOfLetters)
        }
    }

    open fun verifieLettreDansMot(lettre: String, mot: String) {
        for (letter in mot) {
            if (letter == lettre.get(0)) {
                var tv = word_container.getChildAt(letter.toInt()) as TextView?
                tv?.text = letter.toString()
                found++
            }
        }
    }

    open fun afficheLettres(listofletters: CharArrayWriter) {
        val chaine: String = ""
        val result: String = ""
        listofletters.write(chaine)
        for (lettre in chaine) {
            result.plus(lettre + "\n")
        }
        tv_lettres_tapees.text = result

    }
}
