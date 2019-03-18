package fr.isen.toiletgame

//import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_morpion.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.random.Random

class MorpionActivity : AppCompatActivity() {

    class Joueur(var nom: String, @DrawableRes var symbole: Int)

    //lateinit var Cases: Array<View>
    private lateinit var case1: ImageView
    private lateinit var case2: ImageView
    private lateinit var case3: ImageView
    private lateinit var case4: ImageView
    private lateinit var case5: ImageView
    private lateinit var case6: ImageView
    private lateinit var case7: ImageView
    private lateinit var case8: ImageView
    private lateinit var case9: ImageView

    private lateinit var avertisseurJoueurEnCours: TextView
    private lateinit var User: ArrayList<Joueur>
    private lateinit var buttonRelancer: Button
    private var NumeroDuJoueurEnCour = 0

    private lateinit var tableau: Array<Int>

    private lateinit var BoutonJouer: Button
    private lateinit var PseudoField: EditText
    private var Ia : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morpion)

        PseudoField = findViewById(R.id.pseudoField)
        BoutonJouer = findViewById(R.id.buttonJouer)

        //PseudoField.onChange()
        //verifierNomJoueur()

        case1 = findViewById(R.id.Case1)
        case2 = findViewById(R.id.Case2)
        case3 = findViewById(R.id.Case3)
        case4 = findViewById(R.id.Case4)
        case5 = findViewById(R.id.Case5)
        case6 = findViewById(R.id.Case6)
        case7 = findViewById(R.id.Case7)
        case8 = findViewById(R.id.Case8)
        case9 = findViewById(R.id.Case9)

        buttonRelancer = findViewById(R.id.buttonRelancer)

        avertisseurJoueurEnCours = findViewById(R.id.auTourDe)

        val plateau = arrayListOf(case1, case2, case3, case4, case5, case6, case7, case8, case9)

        buttonJouer.setOnClickListener {
            if (pseudoField.text.isNotEmpty())
            {
                initialiserLaPartie(plateau)
            }
            else{
                Toast.makeText(  this,  "Il faut rentrer un pseudo.", Toast.LENGTH_LONG).show()
            }
        }

        buttonRelancer.setOnClickListener {
                if (pseudoField.text.isNotEmpty())
                {
                    initialiserLaPartie(plateau)
                }
                else{
                    Toast.makeText(  this,  "Il faut rentrer un pseudo.", Toast.LENGTH_LONG).show()
                }
        }

        buttonRegles.setOnClickListener {
                val alertDialog = AlertDialog.Builder(this@MorpionActivity)
                alertDialog.setTitle("Règles du jeu")
                alertDialog.setMessage("Le premier joueur a aligner 3 symboles identiques gagne la partie ! \nAttention, le joueur qui débute est avantagé pour gagner... et l'IA déteste perdre... \nBon courage !")
                alertDialog.setNeutralButton("Ok"){_,_ -> }
                alertDialog.create().show()
        }
    }


    fun verifierNomJoueur() {
           // buttonJouer.isEnabled = true
        }


    private fun EditText.onChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                verifierNomJoueur()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun initialiserLaPartie(plateau: ArrayList<ImageView>) { //avant ArrayList<Button>
        //buttonRelancer.isEnabled = false
        NumeroDuJoueurEnCour = 0
        var pseudo1 = pseudoField.getText().toString();
        User = initialisationOrdreJoueur("$pseudo1", "ORDINATEUR")

        //initialisé avec des valeurs autre que 0 ou 1
        tableau = arrayOf(5, 5, 5, 5, 5, 5, 5, 5, 5)

        loop@ plateau.forEach {
            it.setImageResource(R.drawable.blanc)
            it.isClickable = true
        }

        tourDeJeu(plateau)

    }

    // le joueur1 commence toujours. Suivant le random, joueur1 n'a pas le même pseudo
    private fun initialisationOrdreJoueur(pseudo1: String, pseudo2: String): ArrayList<Joueur> {
        val nbAleatoire = Random.nextInt(0, 2)

        if (nbAleatoire == 1) {
            val joueur1 = Joueur(pseudo2, R.drawable.nouveaurond)
            Ia = 0
            val joueur2 = Joueur(pseudo1, R.drawable.nouvellecroix)
            val joueurs = arrayListOf<Joueur>(joueur1, joueur2)
            return joueurs
        } else {
            val joueur1 = Joueur(pseudo1, R.drawable.nouveaurond)
            val joueur2 = Joueur(pseudo2, R.drawable.nouvellecroix)
            Ia = 1
            val joueurs = arrayListOf<Joueur>(joueur1, joueur2)
            return joueurs
        }


    }

    private fun tourDeJeu(plateau: ArrayList<ImageView>) {
        var nbCaseRemplies = 0
        if(Ia == 0){
            tourIA()
            nbCaseRemplies++
        }

        avertisseurJoueurEnCours.text = "C'EST AU TOUR DE : " + User[NumeroDuJoueurEnCour].nom
        loop@ plateau.forEach {

            it.setOnClickListener {

                (it as ImageView).setImageResource(User[NumeroDuJoueurEnCour].symbole)
                it.isClickable = false
                remplirPlateau(it as ImageView)
                nbCaseRemplies++
                checkEgality(nbCaseRemplies)

                if (!verification3MemeSymbole()) {
                    changeJoueur()
                    tourIA()
                    nbCaseRemplies++
                    checkEgality(nbCaseRemplies)
                } else {
                    avertisseurJoueurEnCours.text = User[NumeroDuJoueurEnCour].nom + ", BRAVO ! Vous remportez la partie !"
                    buttonRelancer.isEnabled = true
                }
            }
        }
    }

    private fun checkEgality(nbCaseRemplies: Int){
        if ((nbCaseRemplies == 9)) {
            avertisseurJoueurEnCours.text = " C'est trop triste... Pas de gagnant..."
            buttonRelancer.isEnabled = true
        }
    }

    private fun tourIA() {
        val caseVoid = checkWhereVoid()
        for(case in caseVoid){
            tableau[case] = NumeroDuJoueurEnCour
            if(verification3MemeSymbole()){
                avertisseurJoueurEnCours.text = User[NumeroDuJoueurEnCour].nom + ", BRAVO ! Vous remportez la partie !"
                buttonRelancer.isEnabled = true
                rempliTableau(case)
                return
            }else{
                tableau[case] = 5
            }
        }
        changeJoueur()
        for(case in caseVoid){
            tableau[case] = NumeroDuJoueurEnCour
            if(verification3MemeSymbole()){
                tableau[case] = 5
                changeJoueur()
                tableau[case] = NumeroDuJoueurEnCour
                rempliTableau(case)
                changeJoueur()
                return
            }else{
                tableau[case] = 5
            }
        }
        changeJoueur()
        if(caseVoid.isNotEmpty()){
            val cacase = caseVoid.random()
            tableau[cacase] = NumeroDuJoueurEnCour
            rempliTableau(cacase)
            changeJoueur()
        }
    }

    private fun changeJoueur(){
        if (NumeroDuJoueurEnCour == 0) {
            NumeroDuJoueurEnCour = 1

        } else {
            NumeroDuJoueurEnCour = 0
        }
    }
    private fun checkWhereVoid():ArrayList<Int>{
        val array = ArrayList<Int>()
        for(i in 0..8){
            if(tableau[i] != 0 && tableau[i] != 1){
                array.add(i)
            }
        }
        return array
    }

    //fonction pour savoir quel joueur à rempli quelle case
    private fun remplirPlateau(idImageView: ImageView) {

        when (idImageView.id) {
            R.id.Case1 -> {
                tableau[0] = NumeroDuJoueurEnCour
            }
            R.id.Case2 -> {
                tableau[1] = NumeroDuJoueurEnCour
            }
            R.id.Case3 -> {
                tableau[2] = NumeroDuJoueurEnCour
            }
            R.id.Case4 -> {
                tableau[3] = NumeroDuJoueurEnCour
            }
            R.id.Case5 -> {
                tableau[4] = NumeroDuJoueurEnCour
            }
            R.id.Case6 -> {
                tableau[5] = NumeroDuJoueurEnCour
            }
            R.id.Case7 -> {
                tableau[6] = NumeroDuJoueurEnCour
            }
            R.id.Case8 -> {
                tableau[7] = NumeroDuJoueurEnCour
            }
            R.id.Case9 -> {
                tableau[8] = NumeroDuJoueurEnCour
            }
        }
    }

    private fun setImage(idImageView: ImageView){
        (idImageView as ImageView).setImageResource(User[NumeroDuJoueurEnCour].symbole)
        idImageView.isClickable = false
        remplirPlateau(idImageView as ImageView)
    }

    private fun rempliTableau(case : Int){
        when (case) {
            0 -> {
                setImage(Case1)
            }
            1 -> {
                setImage(Case2)
            }
            2 -> {
                setImage(Case3)
            }
            3 -> {
                setImage(Case4)
            }
            4 -> {
                setImage(Case5)
            }
            5 -> {
                setImage(Case6)
            }
            6 -> {
                setImage(Case7)
            }
            7 -> {
                setImage(Case8)
            }
            8 -> {
                setImage(Case9)
            }

        }
    }

    //fonctions pour vérifier l'alignement des symboles dans tous les cas

    private fun verification3MemeSymboleDiagonal(): Boolean {
        if (tableau[0] == NumeroDuJoueurEnCour && tableau[4] == NumeroDuJoueurEnCour && tableau[8] == NumeroDuJoueurEnCour) {
            return true
        }
        if (tableau[2] == NumeroDuJoueurEnCour && tableau[4] == NumeroDuJoueurEnCour && tableau[6] == NumeroDuJoueurEnCour) {
            return true
        }

        return false
    }

    private fun verification3MemeSymboleHorizontale(): Boolean {
        if (tableau[0] == NumeroDuJoueurEnCour && tableau[3] == NumeroDuJoueurEnCour && tableau[6] == NumeroDuJoueurEnCour) {
            return true
        }
        if (tableau[1] == NumeroDuJoueurEnCour && tableau[4] == NumeroDuJoueurEnCour && tableau[7] == NumeroDuJoueurEnCour) {
            return true
        }
        if (tableau[2] == NumeroDuJoueurEnCour && tableau[5] == NumeroDuJoueurEnCour && tableau[8] == NumeroDuJoueurEnCour) {
            return true
        }

        return false

    }

    private fun verification3MemeSymboleVertical(): Boolean {
        if (tableau[0] == NumeroDuJoueurEnCour && tableau[1] == NumeroDuJoueurEnCour && tableau[2] == NumeroDuJoueurEnCour) {
            return true
        }
        if (tableau[3] == NumeroDuJoueurEnCour && tableau[4] == NumeroDuJoueurEnCour && tableau[5] == NumeroDuJoueurEnCour) {
            return true
        }
        if (tableau[6] == NumeroDuJoueurEnCour && tableau[7] == NumeroDuJoueurEnCour && tableau[8] == NumeroDuJoueurEnCour) {
            return true
        }

        return false
    }

    private fun verification3MemeSymbole(): Boolean =
        verification3MemeSymboleDiagonal() ||
                verification3MemeSymboleVertical() ||
        verification3MemeSymboleHorizontale()


}
