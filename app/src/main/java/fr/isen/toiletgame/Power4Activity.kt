package fr.isen.toiletgame

import android.app.AlertDialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_power4.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import kotlin.math.max
import kotlin.math.min


private const val VOID = 0
private const val RED = 1
private const val YELLOW = 2

class Power4Activity : AppCompatActivity() {

    private var power4 =
        arrayOf(arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0)
        )



    private var ia = true
    private var joueurActuelle = RED
    private var endGame = false

    private val adapters = ArrayList<MyAdapter>()
    private val columns = ArrayList<ListView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power4)

        createGameBoard()
        playMode()

        nouvellePartie.setOnClickListener{
            onNewGame()
        }

        regles.setOnClickListener {
            showRule()
        }
    }

    private fun playMode(){
        val alertDialog = AlertDialog.Builder(this@Power4Activity)
        alertDialog.setTitle("Mode de jeu")
        alertDialog.setMessage("Dans quel mode voulez-vous jouer?")
        alertDialog.setPositiveButton("Joueur VS Joueur"){_,_ ->
            ia = false
            titre.setText(R.string.Puissance4_1V1)
            Joueur.setText(R.string.JoueurRouge)
            Joueur.setTextColor(Color.parseColor("#FF0000"))
        }
        alertDialog.setNegativeButton("Joueur VS IA"){_,_ ->
            ia = true
            titre.setText(R.string.Puissance4_IA)
            Joueur.setText(R.string.JoueurRouge)
            Joueur.setTextColor(Color.parseColor("#FF0000"))
        }
        alertDialog.create().show()
    }

    private fun finDePartie(victor: String){
        endGame = true
        val alertDialog = AlertDialog.Builder(this@Power4Activity)
        alertDialog.setTitle("Fin de la partie")
        alertDialog.setMessage(victor)
        alertDialog.setNeutralButton("Ok"){_,_ ->}
        alertDialog.create().show()
    }

    private fun showRule(){
        val alertDialog = AlertDialog.Builder(this@Power4Activity)
        alertDialog.setTitle("Règles du puissance 4")
        alertDialog.setMessage("Vous devez aligner 4 jetons de votre couleur sur une ligne, une colonne ou une diagonale pour gagner.\n" +
                "Vous placez, à tour de rôle avec votre opposant, un jeton de votre couleur.\n" +
                "Pour placer un jeton, cliquez sur la colonne où vous souhaitez le mettre.")
        alertDialog.setNeutralButton("Ok"){_,_ ->}
        alertDialog.create().show()
    }

    private fun createGameBoard(){
        adapters.add(MyAdapter(this, power4[0]))
        adapters.add(MyAdapter(this, power4[1]))
        adapters.add(MyAdapter(this, power4[2]))
        adapters.add(MyAdapter(this, power4[3]))
        adapters.add(MyAdapter(this, power4[4]))
        adapters.add(MyAdapter(this, power4[5]))
        adapters.add(MyAdapter(this, power4[6]))

        columns.add(column0)
        columns.add(column1)
        columns.add(column2)
        columns.add(column3)
        columns.add(column4)
        columns.add(column5)
        columns.add(column6)

        for (i in 0..6){
            columns[i].adapter = adapters[i]
            columns[i].onItemClickListener = OnItemClickListener{ _, _, _, _ ->
                onPlay(i)
            }
        }
    }

    private fun onPlay(column: Int){
        val ligne = getFirstLigneVoid(column)
        if (ligne != null && !endGame){
            power4[column][ligne] = joueurActuelle
            if (!checkWin(column,ligne)){
                if(ia){
                    onPlayIA()
                }else{
                    if(joueurActuelle == RED){
                        joueurActuelle = YELLOW
                        Joueur.setText(R.string.JoueurJaune)
                        Joueur.setTextColor(Color.parseColor("#FFC73B"))
                    }else{
                        joueurActuelle = RED
                        Joueur.setText(R.string.JoueurRouge)
                        Joueur.setTextColor(Color.parseColor("#FF0000"))
                    }
                }
                if(checkWhereItIsPossibleToPlay().isEmpty()){
                    finDePartie("Egalité")
                }
            }else{
                if(joueurActuelle == RED){
                    finDePartie("Les rouges ont gagné")
                }else{
                    finDePartie("Les jaunes ont gagné")
                }
            }
        }

        chechIfUpdate()
    }

    private fun onNewGame(){
        playMode()
        for(i in 0..6) {
            for (j in 0..5) {
                power4[i][j] = VOID
                joueurActuelle = RED
                endGame = false
            }
        }
        chechIfUpdate()
    }

    private fun chechIfUpdate(){
        for(adapter in adapters){
            adapter.notifyDataSetChanged()
        }
    }

    private fun getFirstLigneVoid(column : Int): Int?{
        for (i in 5 downTo 0) {
            if (power4[column][i] == VOID){
                return i
            }
        }
        return null
    }

    private fun onPlayIA(){
        var column = checkCanWin(YELLOW)
        if(column != null){
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW
            finDePartie("L'IA a gagné")
            return
        }
        column = checkCanWin(RED)
        if (column != null) {
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW
            return
        }

        val whereICanPlayWithoutLosing = checkWhereCanPlayWithoutLosing()
        if(whereICanPlayWithoutLosing.isEmpty()){
            column = checkWhereItIsPossibleToPlay().random()
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW
        }else{
            column = whereICanPlayWithoutLosing.random()
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW
        }        
    }


    private fun checkWhereItIsPossibleToPlay():ArrayList<Int>{
        val tableau = ArrayList<Int>()
        for (i in 0..6){
            val ligne = getFirstLigneVoid(i)
            if (ligne != null){
                tableau.add(i)
            }
        }
        return tableau
    }

    private fun checkCanWin(color: Int): Int?{
        for(column in 0..6){
            val ligne = getFirstLigneVoid(column)
            if (ligne != null){
                power4[column][ligne] = color
                if(checkWin(column,ligne)){
                    power4[column][ligne] = VOID
                    return column
                }else{
                    power4[column][ligne] = VOID
                }
            }
        }
        return null
    }

    private fun checkWhereCanPlayWithoutLosing() : ArrayList<Int>{
        val tableau = ArrayList<Int>()
        for (i in 0..6){
            val ligne = getFirstLigneVoid(i)
            if (ligne != null){
                power4[i][ligne] = YELLOW
                val column = checkCanWin(RED)
                if(column == null){
                    tableau.add(i)
                }
                power4[i][ligne] = VOID
            }
        }
        return tableau
    }

    private fun checkWin(column: Int,ligne: Int): Boolean{
        val minC = max(column-3,0)
        val maxC = min(column+3,6)
        val minL = max(ligne-3,0)
        val maxL = min(ligne+3,5)

        val numbers: IntArray = intArrayOf(0, 0, 0, 0)

        for(i in minC..maxC){

            if(power4[i][ligne] == power4[column][ligne]){
                numbers[0] ++
            }else{
                if(numbers[0]<4){
                    numbers[0] = 0
                }
            }
        }

        for (i in -3..3){
            if(checkIfInside(column+i,ligne+i)) {
                if (power4[column + i][ligne + i] == power4[column][ligne]) {
                    numbers[2]++
                } else {
                    if (numbers[2] < 4) {
                        numbers[2] = 0
                    }
                }
            }
            if(checkIfInside(column+i,ligne-i)) {
                if (power4[column + i][ligne - i] == power4[column][ligne]) {
                    numbers[3]++
                } else {
                    if (numbers[3] < 4) {
                        numbers[3] = 0
                    }
                }
            }
        }


        for(i in minL..maxL){
            if(power4[column][i] == power4[column][ligne]){
                numbers[1] ++
            }else{
                if(numbers[1] < 4){
                    numbers[1] = 0
                }
            }
        }

        if(numbers.any {it > 3 }){
            return true
        }
        return false
    }


    private fun checkIfInside(column: Int, ligne: Int):Boolean{
        return (column < power4.size && column >= 0 && ligne < power4[0].size && ligne >= 0 )
    }


}
