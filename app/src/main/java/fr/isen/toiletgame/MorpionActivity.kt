package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_morpion.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MorpionActivity : AppCompatActivity() {

    class Joueur(var nom: String, @DrawableRes var symbole: Int)

    private lateinit var user: ArrayList<Joueur>
    private var numeroDuJoueurEnCour = 0

    private lateinit var tableau: Array<Int>

    private var ia : Int = 0
    private var endgame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morpion)


        val plateau = arrayListOf(Case1, Case2, Case3, Case4, Case5, Case6, Case7, Case8, Case9)

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
                alertDialog.setMessage("Le premier joueur a aligner 3 symboles identiques gagne la partie ! \nAttention, le joueur qui débute est avantagé pour gagner... et l'ia déteste perdre... \nBon courage !")
                alertDialog.setNeutralButton("Ok"){_,_ -> }
                alertDialog.create().show()
        }
    }


    private fun initialiserLaPartie(plateau: ArrayList<ImageView>) { //avant ArrayList<Button>
        //buttonRelancer.isEnabled = false
        numeroDuJoueurEnCour = 0
        endgame = false
        val pseudo1 = pseudoField.text.toString()
        user = initialisationOrdreJoueur(pseudo1, "ORDINATEUR")

        //initialisé avec des valeurs autre que 0 ou 1
        tableau = arrayOf(5, 5, 5, 5, 5, 5, 5, 5, 5)

        plateau.forEach {
            it.setImageResource(R.drawable.blanc)
            it.isClickable = true
        }

        tourDeJeu(plateau)

    }

    // le joueur1 commence toujours. Suivant le random, joueur1 n'a pas le même pseudo
    private fun initialisationOrdreJoueur(pseudo1: String, pseudo2: String): ArrayList<Joueur> {
        val nbAleatoire = Random.nextInt(0, 2)
        val joueurs = ArrayList<Joueur>()
        if (nbAleatoire == 1) {
            joueurs.add(Joueur(pseudo2, R.drawable.nouveaurond))
            joueurs.add(Joueur(pseudo1, R.drawable.nouvellecroix))
            ia = 0
        } else {
            joueurs.add(Joueur(pseudo1, R.drawable.nouveaurond))
            joueurs.add(Joueur(pseudo2, R.drawable.nouvellecroix))
            ia = 1
        }
        return joueurs


    }

    private fun tourDeJeu(plateau: ArrayList<ImageView>) {
        var nbCaseRemplies = 0
        if(ia == 0){
            tourIa()
            nbCaseRemplies++
        }
        auTourDe.text = "C'EST AU TOUR DE : " + user[numeroDuJoueurEnCour].nom

        plateau.forEach {

            it.setOnClickListener {
                if(!endgame) {
                    (it as ImageView).setImageResource(user[numeroDuJoueurEnCour].symbole)
                    it.isClickable = false
                    remplirPlateau(it)
                    nbCaseRemplies++
                    checkEgality(nbCaseRemplies)

                    if (!verification3MemeSymbole()) {
                        changeJoueur()
                        tourIa()
                        nbCaseRemplies++
                        checkEgality(nbCaseRemplies)
                    } else {
                        auTourDe.text = user[numeroDuJoueurEnCour].nom +
                                ", BRAVO ! Vous remportez la partie !"
                        buttonRelancer.isEnabled = true
                    }
                }
            }
        }
    }

    private fun checkEgality(nbCaseRemplies: Int){
        if ((nbCaseRemplies == 9)) {
            auTourDe.setText(R.string.NoWinner)
            buttonRelancer.isEnabled = true
        }
    }

    private fun tourIa() {
        if(checkIfCanWin()){
            return
        }
        if(checkIfPlayerCanWin()){
            return
        }
        playWhereiaCan()
    }

    private fun checkIfCanWin() : Boolean{
        val caseVoid = checkWhereVoid()
        for(case in caseVoid){
            tableau[case] = numeroDuJoueurEnCour
            if(verification3MemeSymbole()){
                auTourDe.text = user[numeroDuJoueurEnCour].nom + ", BRAVO ! Vous remportez la partie !"
                buttonRelancer.isEnabled = true
                rempliTableau(case)
                endgame = true
                return true
            }else{
                tableau[case] = 5
            }
        }
        return false
    }

    private fun checkIfPlayerCanWin() : Boolean{
        val caseVoid = checkWhereVoid()
        changeJoueur()
        for(case in caseVoid){
            tableau[case] = numeroDuJoueurEnCour
            if(verification3MemeSymbole()){
                tableau[case] = 5
                changeJoueur()
                tableau[case] = numeroDuJoueurEnCour
                rempliTableau(case)
                changeJoueur()
                return true
            }else{
                tableau[case] = 5
            }
        }
        return false
    }

    private fun playWhereiaCan(){
        val caseVoid = checkWhereVoid()
        changeJoueur()
        if(caseVoid.isNotEmpty()){
            val case = caseVoid.random()
            tableau[case] = numeroDuJoueurEnCour
            rempliTableau(case)
            changeJoueur()
        }
    }

    private fun changeJoueur(){
        if(numeroDuJoueurEnCour == 0){
            numeroDuJoueurEnCour = 1
        }else{
            numeroDuJoueurEnCour = 0
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
                tableau[0] = numeroDuJoueurEnCour
            }
            R.id.Case2 -> {
                tableau[1] = numeroDuJoueurEnCour
            }
            R.id.Case3 -> {
                tableau[2] = numeroDuJoueurEnCour
            }
            R.id.Case4 -> {
                tableau[3] = numeroDuJoueurEnCour
            }
            R.id.Case5 -> {
                tableau[4] = numeroDuJoueurEnCour
            }
            R.id.Case6 -> {
                tableau[5] = numeroDuJoueurEnCour
            }
            R.id.Case7 -> {
                tableau[6] = numeroDuJoueurEnCour
            }
            R.id.Case8 -> {
                tableau[7] = numeroDuJoueurEnCour
            }
            R.id.Case9 -> {
                tableau[8] = numeroDuJoueurEnCour
            }
        }
    }

    private fun setImage(idImageView: ImageView){
        idImageView.setImageResource(user[numeroDuJoueurEnCour].symbole)
        idImageView.isClickable = false
        remplirPlateau(idImageView)
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
        if (tableau[0] == numeroDuJoueurEnCour && tableau[4] == numeroDuJoueurEnCour && tableau[8] == numeroDuJoueurEnCour) {
            return true
        }
        if (tableau[2] == numeroDuJoueurEnCour && tableau[4] == numeroDuJoueurEnCour && tableau[6] == numeroDuJoueurEnCour) {
            return true
        }

        return false
    }

    private fun verification3MemeSymboleHorizontale(): Boolean {
        if (tableau[0] == numeroDuJoueurEnCour && tableau[3] == numeroDuJoueurEnCour && tableau[6] == numeroDuJoueurEnCour) {
            return true
        }
        if (tableau[1] == numeroDuJoueurEnCour && tableau[4] == numeroDuJoueurEnCour && tableau[7] == numeroDuJoueurEnCour) {
            return true
        }
        if (tableau[2] == numeroDuJoueurEnCour && tableau[5] == numeroDuJoueurEnCour && tableau[8] == numeroDuJoueurEnCour) {
            return true
        }

        return false

    }

    private fun verification3MemeSymboleVertical(): Boolean {
        if (tableau[0] == numeroDuJoueurEnCour && tableau[1] == numeroDuJoueurEnCour && tableau[2] == numeroDuJoueurEnCour) {
            return true
        }
        if (tableau[3] == numeroDuJoueurEnCour && tableau[4] == numeroDuJoueurEnCour && tableau[5] == numeroDuJoueurEnCour) {
            return true
        }
        if (tableau[6] == numeroDuJoueurEnCour && tableau[7] == numeroDuJoueurEnCour && tableau[8] == numeroDuJoueurEnCour) {
            return true
        }

        return false
    }

    private fun verification3MemeSymbole(): Boolean =
        verification3MemeSymboleDiagonal() ||
                verification3MemeSymboleVertical() ||
        verification3MemeSymboleHorizontale()


}
