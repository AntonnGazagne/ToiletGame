package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MorpionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morpion)
    }
}

fun lancementPartie (plateau:ArrayList<Button>){

    //buttonRelance.isEnabled = false
}