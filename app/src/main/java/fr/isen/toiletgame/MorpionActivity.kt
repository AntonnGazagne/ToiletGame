package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_morpion.*

class MorpionActivity : AppCompatActivity() {

    lateinit var BoutonJouer:Button
    lateinit var PseudoField:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morpion)

        PseudoField = findViewById(R.id.pseudoField)
        BoutonJouer  = findViewById(R.id.buttonJouer)

        PseudoField.onChange()

    }

    fun VerifierNomJoueur() {
        if (!(pseudoField.text.isEmpty())) {


            buttonJouer.isEnabled = true
        }
    }

    fun EditText.onChange () {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { VerifierNomJoueur()  }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
