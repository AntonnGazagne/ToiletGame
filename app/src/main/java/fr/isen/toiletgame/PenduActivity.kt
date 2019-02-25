package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pendu.*

class PenduActivity : AppCompatActivity() {

    val listOfLetters: MutableList<Char>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu)

        initGame()

        btn_send.setOnClickListener { envoiLettre() }

    }

    fun initGame() {
        val word = "ordinateur"
        var win = false
        val error = 0
        val found = 0
        tv_lettres_tapees.setText("")
        iv_pendu.setBackgroundResource(R.drawable.first)

        word_container.removeAllViews()

        for (lettre in word) {
            var oneLetter = layoutInflater.inflate(R.layout.textview, null) as TextView?
            word_container.addView(oneLetter)
        }

    }

    fun envoiLettre() {
        val lettreFromInput = et_letter.text.toString().toUpperCase()
        et_letter.setText("")

        if (lettreFromInput.isNotEmpty()) {
            //if (!lettreDejaUtilisee(lettreFromInput.get(0), listOfLetters))
        }
    }

    fun lettreDejaUtilisee(a: Char, listOfLetters: MutableList<Char>): Boolean {
        for (lettre in listOfLetters) {
            if (listOfLetters.get(lettre.toInt()) == a) {
                Toast.makeText(this, "Vous avez déjà entré cette lettre", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }
}
