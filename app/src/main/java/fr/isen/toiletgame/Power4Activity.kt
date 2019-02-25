package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_power4.*




class Power4Activity : AppCompatActivity() {

    var power4 =
        arrayOf(arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0)
        )

    val VOID = 0
    val RED = 1
    val YELLOW = 2

    var joueur = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power4)

        for(i in 0..6){
            for(j in 0..5){
                var id = "image" + i.toString() + j.toString()
                val res = R.drawable::class.java
                val field = res.getField(id)
                val drawableId = field.getInt(null)
                var image = findViewById<ImageView>(drawableId)

                image.setOnClickListener {
                    if (joueur) {//Si c'est au joueur
                        onPlay(i, RED)
                    }
                }
            }
        }
        nouvellePartie.setOnClickListener{
            onNewGame()
        }
    }


    private fun onPlay(column: Int,token: Int){
        var ligne = getFirstLigneVoid(column)
        if (ligne != null){
            addToken(column, ligne, token)
            joueur = !joueur//Ce n'est plus au joueur

            onPlayIA()

        }else{
            Toast.makeText(this, "La colomne est pleine", Toast.LENGTH_LONG).show()
        }
    }

    //On réinitialise les valeur
    private fun onNewGame(){
        for(i in 0..6){
            for(j in 0..5){
                addToken(i, j, VOID)
                joueur = true
            }
        }
    }

    private fun getFirstLigneVoid(column : Int): Int?{
        for (i in 0..5) {
            if (power4[column][i] == VOID){
                return i
            }
        }
        return null
    }

    private fun onPlayIA(){

    }

    private fun checkWin(column: Int,ligne: Int){
        var minC = column-3
        var maxC = column+3
        if(minC < 0){
            minC=0
        }
        if(maxC > 6){
            maxC=6
        }
        var minL = ligne-3
        var maxL = ligne+3
        if(minL < 0){
            minL=0
        }
        if(maxL > 6){
            maxL=6
        }
        for(i in minC..maxC){

        }
    }

    private fun addToken(column: Int, ligne: Int,color: Int){
        var id = "R.id.image" + column.toString() + ligne.toString()
        val res = R.drawable::class.java
        val field = res.getField(id)
        val drawableId = field.getInt(null)
        var image = findViewById<ImageView>(drawableId)

        if(color == VOID){
            val resD = resources.getIdentifier("R.drawable.vide","drawable",packageName)
            image.setImageResource(resD)
        }
        if(color == RED){
            val res = resources.getIdentifier("R.drawable.red","drawable",packageName)
            image.setImageResource(res)
        }
        if(color == YELLOW){
            val res = resources.getIdentifier("R.drawable.yellow","drawable",packageName)
            image.setImageResource(res)
        }
        power4[column][ligne] = color

        //checkWin(column,ligne)
    }
}
