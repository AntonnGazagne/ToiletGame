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
                var id = "R.id.image" + i.toString() + j.toString()
                var image = findViewById<ImageView>(id.toInt())
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
    private fun addToken(column: Int, ligne: Int,color: Int){

    }

    private fun setCurseur(){

    }
}
