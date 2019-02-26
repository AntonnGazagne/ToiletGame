package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_power4.*
import android.widget.AdapterView.OnItemClickListener
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



    private var IA = true
    private var joueurActuelle = RED
    private var endGame = false
    
    private val adapters = ArrayList<MyAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power4)
        
        
        adapters.add(MyAdapter(this, power4[0]))
        adapters.add(MyAdapter(this, power4[1]))
        adapters.add(MyAdapter(this, power4[2]))
        adapters.add(MyAdapter(this, power4[3]))
        adapters.add(MyAdapter(this, power4[4]))
        adapters.add(MyAdapter(this, power4[5]))
        adapters.add(MyAdapter(this, power4[6]))



        column0.adapter = adapters[0]
        column0.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(0)
        }

        column1.adapter = adapters[1]
        column1.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(1)
        }
        
        column2.adapter = adapters[2]
        column2.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(2)
        }

        column3.adapter = adapters[3]
        column3.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(3)
        }

        column4.adapter = adapters[4]
        column4.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(4)
        }

        column5.adapter = adapters[5]
        column5.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(5)
        }


        column6.adapter = adapters[6]
        column6.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(6)
        }

        nouvellePartieIA.setOnClickListener{
            IA = true
            onNewGame()
        }
        nouvellePartieJoueur.setOnClickListener{
            IA = false
            onNewGame()
        }
    }


    private fun onPlay(column: Int){
        val ligne = getFirstLigneVoid(column)

        if (ligne != null && !endGame){
            power4[column][ligne] = joueurActuelle

            if (!checkWin(column,ligne)){
                if(IA){
                    onPlayIA()
                }else{
                    if(joueurActuelle == RED){
                        joueurActuelle = YELLOW
                    }else{
                        joueurActuelle = RED
                    }
                }
                if(checkWhereItIsPossibleToPlay().isEmpty()){
                    endGame = true
                    Toast.makeText(this, "Egalité", Toast.LENGTH_LONG).show()
                }
            }else{
                if(joueurActuelle == RED){
                    Toast.makeText(this, "RED Victory", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "YELLOW Victory", Toast.LENGTH_LONG).show()
                }
                endGame = true
            }
        }
        chechIfUpdate()
    }

    //On réinitialise les valeur
    private fun onNewGame(){
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
        var column = checkCanWin(YELLOW)//On vérifie si l'IA peut gagner
        if(column != null){
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW
            endGame = true
            Toast.makeText(this, "You Lose", Toast.LENGTH_LONG).show()
            return
        }
        column = checkCanWin(RED)//On vérifie si le joueur peut gagner
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

        val numbers: IntArray = intArrayOf(0, 0, 0, 0)//Ligne,Colonne,Diagonale Haut-Bas, Diagonale Bas-Haut

        for(i in minC..maxC){

            if(power4[i][ligne] == power4[column][ligne]){//On check sur la ligne
                numbers[0] ++
            }else{
                if(numbers[0]<4){
                    numbers[0] = 0
                }
            }
        }

        for (i in -3..3){
            if(checkIfInside(column+i,ligne+i)) {
                if (power4[column + i][ligne + i] == power4[column][ligne]) {//On check sur la ligne
                    numbers[2]++
                } else {
                    if (numbers[2] < 4) {
                        numbers[2] = 0
                    }
                }
            }
            if(checkIfInside(column+i,ligne-i)) {
                if (power4[column + i][ligne - i] == power4[column][ligne]) {//On check sur la ligne
                    numbers[3]++
                } else {
                    if (numbers[3] < 4) {
                        numbers[3] = 0
                    }
                }
            }
        }


        for(i in minL..maxL){
            if(power4[column][i] == power4[column][ligne]){//On check sur la colonne
                numbers[1] ++
            }else{
                if(numbers[1] < 4){
                    numbers[1] = 0
                }
            }
        }

        if(numbers.filter{it > 3 }.isNotEmpty()){
            return true
        }
        return false
    }

    private fun checkIfInside(column: Int, ligne: Int):Boolean{
        return (column < power4.size && column >= 0 && ligne < power4[0].size && ligne >= 0 )
    }


}
