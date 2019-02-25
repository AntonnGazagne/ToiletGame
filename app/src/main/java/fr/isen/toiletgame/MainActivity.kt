package fr.isen.toiletgame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        p4IcView.setOnClickListener{
            goPower()
        }

        yamsIcView.setOnClickListener{
            goYams()
        }

        hangmanIcView.setOnClickListener{
            goHangman()
        }

        hangmanIcView.setOnClickListener{
            goMorpion()
        }


    }

    private fun goPower(){
        val intent = Intent(this@MainActivity, Power4Activity::class.java )
        startActivity(intent)
    }

    private fun goYams(){
        val intent = Intent(this@MainActivity, YathzeeActivity::class.java )
        startActivity(intent)
    }

    private fun goHangman(){
        val intent = Intent(this@MainActivity, PenduActivity::class.java )
        startActivity(intent)
    }

    private fun goMorpion(){
        val intent = Intent(this@MainActivity, MorpionActivity::class.java )
        startActivity(intent)
    }
}
