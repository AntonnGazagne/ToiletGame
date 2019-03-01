package fr.isen.toiletgame

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_yathzee.*

class YathzeeActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorManager: SensorManager
    private var isRolling = 0
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.values[0] + event.values[1] + event.values[2]> 40 && roll < 3 && isRolling == 0){
            isRolling = 1
            for(i in 1..10){
                Handler().postDelayed({
                    var diceValue0 = diceValue[0]
                    var diceValue1 = diceValue[1]
                    var diceValue2 = diceValue[2]
                    var diceValue3 = diceValue[3]
                    var diceValue4 = diceValue[4]

                    if (isKept[0] == 0){ diceValue0 = (1..6).random()}
                    if (isKept[1] == 0){ diceValue1 = (1..6).random()}
                    if (isKept[2] == 0){ diceValue2 = (1..6).random()}
                    if (isKept[3] == 0){ diceValue3 = (1..6).random()}
                    if (isKept[4] == 0){ diceValue4 = (1..6).random()}

                    val resDice1 = "@drawable/dice" + diceValue0.toString()
                    val resDice2 = "@drawable/dice" + diceValue1.toString()
                    val resDice3 = "@drawable/dice" + diceValue2.toString()
                    val resDice4 = "@drawable/dice" + diceValue3.toString()
                    val resDice5 = "@drawable/dice" + diceValue4.toString()

                    val idDice1 = resources.getIdentifier(resDice1, "drawable", packageName)
                    val idDice2 = resources.getIdentifier(resDice2, "drawable", packageName)
                    val idDice3 = resources.getIdentifier(resDice3, "drawable", packageName)
                    val idDice4 = resources.getIdentifier(resDice4, "drawable", packageName)
                    val idDice5 = resources.getIdentifier(resDice5, "drawable", packageName)

                    dice1View.setBackgroundResource(idDice1)
                    dice2View.setBackgroundResource(idDice2)
                    dice3View.setBackgroundResource(idDice3)
                    dice4View.setBackgroundResource(idDice4)
                    dice5View.setBackgroundResource(idDice5)
                    if(i == 10)
                    {
                        rollDices()
                        isRolling = 0
                    }
                }, i.toLong()*200)
            }

        }
    }


    private var diceValue= arrayOf(0,0,0,0,0)
    private var scoreValueLeft= arrayOf(0,0,0,0,0,0)
    private var scoreValueRight= arrayOf(0,0,0,0,0,0,0)
    private var isUsed = arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var isKept= arrayOf(0,0,0,0,0)
    private var roll = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yathzee)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        rollButton.setOnClickListener{
            if(roll < 3 && isRolling == 0){
                for(i in 1..10){
                    Handler().postDelayed({
                        var diceValue0 = diceValue[0]
                        var diceValue1 = diceValue[1]
                        var diceValue2 = diceValue[2]
                        var diceValue3 = diceValue[3]
                        var diceValue4 = diceValue[4]

                        if (isKept[0] == 0){ diceValue0 = (1..6).random()}
                        if (isKept[1] == 0){ diceValue1 = (1..6).random()}
                        if (isKept[2] == 0){ diceValue2 = (1..6).random()}
                        if (isKept[3] == 0){ diceValue3 = (1..6).random()}
                        if (isKept[4] == 0){ diceValue4 = (1..6).random()}

                        val resDice1 = "@drawable/dice" + diceValue0.toString()
                        val resDice2 = "@drawable/dice" + diceValue1.toString()
                        val resDice3 = "@drawable/dice" + diceValue2.toString()
                        val resDice4 = "@drawable/dice" + diceValue3.toString()
                        val resDice5 = "@drawable/dice" + diceValue4.toString()

                        val idDice1 = resources.getIdentifier(resDice1, "drawable", packageName)
                        val idDice2 = resources.getIdentifier(resDice2, "drawable", packageName)
                        val idDice3 = resources.getIdentifier(resDice3, "drawable", packageName)
                        val idDice4 = resources.getIdentifier(resDice4, "drawable", packageName)
                        val idDice5 = resources.getIdentifier(resDice5, "drawable", packageName)

                        dice1View.setBackgroundResource(idDice1)
                        dice2View.setBackgroundResource(idDice2)
                        dice3View.setBackgroundResource(idDice3)
                        dice4View.setBackgroundResource(idDice4)
                        dice5View.setBackgroundResource(idDice5)
                        if(i == 10)
                        {
                            rollDices()
                        }
                    }, i.toLong()*200)
                }
            }
        }

        rulesButton.setOnClickListener{
            goRules()
        }

        dice1View.setOnClickListener(){
            if(roll != 0 && isRolling == 0){
                isKept[0] = 1 - isKept[0]
                var idDice = 0
                if (isKept[0]==1){
                    idDice = resources.getIdentifier("@drawable/dice_selector", "drawable", packageName)
                }
                dice1View.setImageResource(idDice)
            }
        }

        dice2View.setOnClickListener(){
            if(roll != 0 && isRolling == 0){
                isKept[1] = 1 - isKept[1]
                var idDice = 0
                if (isKept[1]==1){
                    idDice = resources.getIdentifier("@drawable/dice_selector", "drawable", packageName)
                }
                dice2View.setImageResource(idDice)
            }
        }

        dice3View.setOnClickListener(){
            if(roll != 0 && isRolling == 0){
                isKept[2] = 1 - isKept[2]
                var idDice = 0
                if (isKept[2]==1){
                    idDice = resources.getIdentifier("@drawable/dice_selector", "drawable", packageName)
                }
                dice3View.setImageResource(idDice)
            }
        }

        dice4View.setOnClickListener(){
            if(roll != 0 && isRolling == 0){
                isKept[3] = 1 - isKept[3]
                var idDice = 0
                if (isKept[3]==1){
                    idDice = resources.getIdentifier("@drawable/dice_selector", "drawable", packageName)
                }
                dice4View.setImageResource(idDice)
            }
        }

        dice5View.setOnClickListener(){
            if(roll != 0 && isRolling == 0){
                isKept[4] = 1 - isKept[4]
                var idDice = 0
                if (isKept[4]==1){
                    idDice = resources.getIdentifier("@drawable/dice_selector", "drawable", packageName)
                }
                dice5View.setImageResource(idDice)
            }
        }

        sum1ScoreView.setOnClickListener{
            if(isUsed[3] == 0 && roll != 0){
                isUsed[0] = 1
                addTotal1(sum1ScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        sum2ScoreView.setOnClickListener{
            if(isUsed[1] == 0 && roll != 0){
                isUsed[1] = 1
                addTotal1(sum2ScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        sum3ScoreView.setOnClickListener{
            if(isUsed[2] == 0 && roll != 0){
                isUsed[2] = 1
                addTotal1(sum3ScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        sum4ScoreView.setOnClickListener{
            if(isUsed[3] == 0 && roll != 0){
                isUsed[3] = 1
                addTotal1(sum4ScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        sum5ScoreView.setOnClickListener{
            if(isUsed[4] == 0 && roll != 0){
                isUsed[4] = 1
                addTotal1(sum5ScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        sum6ScoreView.setOnClickListener{
            if(isUsed[5] == 0 && roll != 0){
                isUsed[5] = 1
                addTotal1(sum6ScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        brelanScoreView.setOnClickListener{
            if(isUsed[6] == 0 && roll != 0){
                isUsed[6] = 1
                addTotal2(brelanScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        carreScoreView.setOnClickListener{
            if(isUsed[7] == 0 && roll != 0){
                isUsed[7] = 1
                addTotal2(carreScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        fullScoreView.setOnClickListener{
            if(isUsed[8] == 0 && roll != 0){
                isUsed[8] = 1
                addTotal2(fullScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        psuiteScoreView.setOnClickListener{
            if(isUsed[9] == 0 && roll != 0){
                isUsed[9] = 1
                addTotal2(psuiteScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        gsuiteScoreView.setOnClickListener{
            if(isUsed[10] == 0 && roll != 0){
                isUsed[10] = 1
                addTotal2(gsuiteScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        yamsScoreView.setOnClickListener{
            if(isUsed[11] == 0 && roll != 0){
                isUsed[11] = 1
                addTotal2(yamsScoreView)
                resetRoll()
                gameOverTest()
            }
        }

        chanceScoreView.setOnClickListener{
            if(isUsed[12] == 0 && roll != 0){
                isUsed[12] = 1
                addTotal2(chanceScoreView)
                resetRoll()
                gameOverTest()
            }
        }

    }

    private fun goRules(){
        val intent = Intent(this@YathzeeActivity, RulesActivity::class.java)
        startActivity(intent)
    }

    private fun diceAnimation(){

    }
    private fun gameOverTest(){
        if(isUsed.filter{it -> it == 0}.isEmpty()){
            gameOver()
        }
    }
    private fun gameOver(){

        scoreValueLeft= arrayOf(0,0,0,0,0,0)
        scoreValueRight= arrayOf(0,0,0,0,0,0,0)
        isUsed = arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0)
        isKept = arrayOf(0,0,0,0,0)
        roll = 0

        sum1ScoreView.setTextColor(Color.parseColor("#FF0000"))
        sum2ScoreView.setTextColor(Color.parseColor("#FF0000"))
        sum3ScoreView.setTextColor(Color.parseColor("#FF0000"))
        sum4ScoreView.setTextColor(Color.parseColor("#FF0000"))
        sum5ScoreView.setTextColor(Color.parseColor("#FF0000"))
        sum6ScoreView.setTextColor(Color.parseColor("#FF0000"))
        brelanScoreView.setTextColor(Color.parseColor("#FF0000"))
        carreScoreView.setTextColor(Color.parseColor("#FF0000"))
        fullScoreView.setTextColor(Color.parseColor("#FF0000"))
        psuiteScoreView.setTextColor(Color.parseColor("#FF0000"))
        gsuiteScoreView.setTextColor(Color.parseColor("#FF0000"))
        yamsScoreView.setTextColor(Color.parseColor("#FF0000"))
        chanceScoreView.setTextColor(Color.parseColor("#FF0000"))

        Toast.makeText(this, "Vous avez terminé avec " + totalScoreView.text.toString() + " points", Toast.LENGTH_LONG).show()

        sum1ScoreView.text = 0.toString()
        sum2ScoreView.text = 0.toString()
        sum3ScoreView.text = 0.toString()
        sum4ScoreView.text = 0.toString()
        sum5ScoreView.text = 0.toString()
        sum6ScoreView.text = 0.toString()
        brelanScoreView.text = 0.toString()
        carreScoreView.text = 0.toString()
        fullScoreView.text = 0.toString()
        psuiteScoreView.text = 0.toString()
        gsuiteScoreView.text = 0.toString()
        yamsScoreView.text = 0.toString()
        chanceScoreView.text = 0.toString()
        brelanScoreView.text = 0.toString()
        total1ScoreView.text = 0.toString()
        total2ScoreView.text = 0.toString()
        totalScoreView.text = 0.toString()


    }
    private fun resetRoll(){
        roll = 0
        isKept = arrayOf(0,0,0,0,0)
        dice1View.setImageResource(0)
        dice2View.setImageResource(0)
        dice3View.setImageResource(0)
        dice4View.setImageResource(0)
        dice5View.setImageResource(0)
        rollButton.setTextColor(Color.parseColor("#000000"))
    }

    private fun addTotal1(scoreView : TextView){
        scoreView.setTextColor(Color.parseColor("#000000"))
        total1ScoreView.text = (total1ScoreView.text.toString().toInt() + scoreView.text.toString().toInt()).toString()

        val bonusValue = bonusScoreView.text.toString().toInt()
        val total1Value = total1ScoreView.text.toString().toInt()
        if(bonusValue == 0 && total1Value >= 63) {
            bonusScoreView.text = 35.toString()
            total1ScoreView.text = (total1ScoreView.text.toString().toInt() + 35).toString()
        }

        doTotal()
    }

    private fun addTotal2(scoreView : TextView){
        scoreView.setTextColor(Color.parseColor("#000000"))
        total2ScoreView.text = (total2ScoreView.text.toString().toInt() + scoreView.text.toString().toInt()).toString()

        doTotal()
    }

    private fun doTotal()
    {
        totalScoreView.text = (total1ScoreView.text.toString().toInt() + total2ScoreView.text.toString().toInt()).toString()
    }

    private fun rollDices(){
        roll++
        if(roll == 3){
            rollButton.setTextColor(Color.parseColor("#BBBBBB"))
        }

        if (isKept[0] == 0){ diceValue[0] = (1..6).random()}
        if (isKept[1] == 0){ diceValue[1] = (1..6).random()}
        if (isKept[2] == 0){ diceValue[2] = (1..6).random()}
        if (isKept[3] == 0){ diceValue[3] = (1..6).random()}
        if (isKept[4] == 0){ diceValue[4] = (1..6).random()}



        var nb1 = 0
        var nb2 = 0
        var nb3 = 0
        var nb4 = 0
        var nb5 = 0
        var nb6 = 0

        for(i in 0..5){
            scoreValueLeft[i] = scoreValueLeft[i] * isUsed[i]
        }

        for(i in 0..6){
            scoreValueRight[i] = scoreValueRight[i] * isUsed[i+6]
        }
        for(i in 0..4){
            if(diceValue[i] == 1){
                nb1++
                if(isUsed[6]==0 && nb1 == 3){
                    scoreValueRight[0] = 3
                }
                else if(isUsed[7]==0 && nb1 == 4){
                    scoreValueRight[1] = 4
                }

            }
            else if(diceValue[i]==2){
                nb2++
                if(isUsed[6]==0 && nb2 == 3){
                    scoreValueRight[0] = 6
                }
                else if(isUsed[7]==0 && nb2 == 4){
                    scoreValueRight[1] = 8
                }

            }
            else if(diceValue[i]==3){
                nb3++
                if(isUsed[6]==0 && nb3 == 3){
                    scoreValueRight[0] = 9
                }
                else if(isUsed[7]==0 && nb3 == 4){
                    scoreValueRight[1] = 12
                }
            }
            else if(diceValue[i]==4){
                nb4++
                if(isUsed[6]==0 && nb4 == 3){
                    scoreValueRight[0] = 12
                }
                else if(isUsed[7]==0 && nb4 == 4){
                    scoreValueRight[1] = 16
                }
            }
            else if(diceValue[i]==5){
                nb5++
                if(isUsed[6]==0 && nb5 == 3){
                    scoreValueRight[0] = 15
                }
                else if(isUsed[7]==0 && nb5 == 4){
                    scoreValueRight[1] = 20
                }
            }
            else{
                nb6++
                if(isUsed[6]==0 && nb6 == 3){
                    scoreValueRight[0] = 18
                }
                else if(isUsed[7]==0 && nb6 == 4){
                    scoreValueRight[1] = 24
                }
            }
        }

        //yams
        if((nb1 == 5 || nb2 == 5 || nb3 == 5 || nb4 == 5 || nb5 == 5 || nb6 == 5) && isUsed[12] == 0)
        {
            scoreValueRight[5] = 50
        }

        //petite suite
        if(((nb1 > 0 && nb2 > 0 && nb3 > 0 && nb4 > 0) || (nb5 > 0 && nb2 > 0 && nb3 > 0 && nb4 > 0) || (nb6 > 0 && nb5 > 0 && nb3 > 0 && nb4 > 0)) && isUsed[9] == 0){
            scoreValueRight[3] = 30
        }

        //grande suite
        if(((nb1 > 0 && nb2 > 0 && nb3 > 0 && nb4 > 0 && nb5 > 0) || (nb6 > 0 && nb5 > 0 && nb2 > 0 && nb3 > 0 && nb4 > 0)) && isUsed[10] == 0){
            scoreValueRight[4] = 40
        }

        //full
        if((nb1 == 3 || nb2 == 3 || nb3 == 3 || nb4 == 3 || nb5 == 3 ||nb6 == 3) && (nb1 == 2 || nb2 == 2 || nb3 == 2 || nb4 == 2 || nb5 == 2 ||nb6 == 2) && isUsed[8] == 0){
            scoreValueRight[2] = 25
        }

        //chance
        if(isUsed[12] == 0){
            scoreValueRight[6] = diceValue[0] + diceValue[1] + diceValue[2] + diceValue[3] + diceValue[4]
        }

        //total 1
        if(isUsed[0] == 0){
            scoreValueLeft[0] = nb1
        }

        //total 2
        if(isUsed[1] == 0){
            scoreValueLeft[1] = nb2*2
        }

        //total 3
        if(isUsed[2] == 0){
            scoreValueLeft[2] = nb3*3
        }

        //total 4
        if(isUsed[3] == 0){
            scoreValueLeft[3] = nb4*4
        }

        //total 5
        if(isUsed[4] == 0){
            scoreValueLeft[4] = nb5*5
        }

        //total 6
        if(isUsed[5] == 0){
            scoreValueLeft[5] = nb6*6
        }

        sum1ScoreView.text = scoreValueLeft[0].toString()
        sum2ScoreView.text = scoreValueLeft[1].toString()
        sum3ScoreView.text = scoreValueLeft[2].toString()
        sum4ScoreView.text = scoreValueLeft[3].toString()
        sum5ScoreView.text = scoreValueLeft[4].toString()
        sum6ScoreView.text = scoreValueLeft[5].toString()
        brelanScoreView.text = scoreValueRight[0].toString()
        carreScoreView.text = scoreValueRight[1].toString()
        fullScoreView.text = scoreValueRight[2].toString()
        psuiteScoreView.text = scoreValueRight[3].toString()
        gsuiteScoreView.text = scoreValueRight[4].toString()
        yamsScoreView.text = scoreValueRight[5].toString()
        chanceScoreView.text = scoreValueRight[6].toString()


        val resDice1 = "@drawable/dice" + diceValue[0].toString()
        val resDice2 = "@drawable/dice" + diceValue[1].toString()
        val resDice3 = "@drawable/dice" + diceValue[2].toString()
        val resDice4 = "@drawable/dice" + diceValue[3].toString()
        val resDice5 = "@drawable/dice" + diceValue[4].toString()

        val idDice1 = resources.getIdentifier(resDice1, "drawable", packageName)
        val idDice2 = resources.getIdentifier(resDice2, "drawable", packageName)
        val idDice3 = resources.getIdentifier(resDice3, "drawable", packageName)
        val idDice4 = resources.getIdentifier(resDice4, "drawable", packageName)
        val idDice5 = resources.getIdentifier(resDice5, "drawable", packageName)

        /*dice1View.setImageResource(idDice1)
        dice2View.setImageResource(idDice2)
        dice3View.setImageResource(idDice3)
        dice4View.setImageResource(idDice4)
        dice5View.setImageResource(idDice5)*/

        dice1View.setBackgroundResource(idDice1)
        dice2View.setBackgroundResource(idDice2)
        dice3View.setBackgroundResource(idDice3)
        dice4View.setBackgroundResource(idDice4)
        dice5View.setBackgroundResource(idDice5)

    }

}
