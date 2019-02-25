package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pendu.*

abstract class PenduActivity : AppCompatActivity() {

    private var win: Boolean = false
    private var error: Int = 0
    private var found: Int = 0
    private val mot: String = "ORDINATEUR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu)

        initGame()

        btn_send.setOnClickListener { envoiLettre() }

    }

    private fun initGame() {
        //mot = "ORDINATEUR"
        tv_lettres_tapees.text = ""
        iv_pendu.setBackgroundResource(R.drawable.first)

        word_container.removeAllViews()

        for (lettre in mot) {
            var oneLetter = layoutInflater.inflate(R.layout.textview, null) as TextView?
            word_container.addView(oneLetter)
        }

    }

    fun envoiLettre() {
        var lettreFromInput: String = et_letter.text.toString().toUpperCase()
        et_letter.text.clear()

        if (lettreFromInput.isNotEmpty()) {
            var listOfLetters = CharArray(mot.length)
            if (!listOfLetters.contains(lettreFromInput.get(0))) {
                listOfLetters.plus(lettreFromInput.get(0))
                VerifieLettreDansMot(lettreFromInput, mot)
            }
            else {
                Toast.makeText(this, "Vous avez déjà entré cette lettre", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun VerifieLettreDansMot(lettre: String, mot: String) {
        for (letter in mot) {
            if (letter == lettre.get(0)) {
                var tv = word_container.getChildAt(letter.toInt()) as TextView?
                tv?.text = letter.toString()
                found++
            }
        }
    }
}
