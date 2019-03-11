package fr.isen.toiletgame

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tetris.*
import java.util.*

private val LEFT = arrayOf(0,-1)
private val RIGHT = arrayOf(0,1)
private val DOWN = arrayOf(1,0)

private const val WHITE = 0

class TetrisActivity : AppCompatActivity() {

    private lateinit var adapter: ColorAdapter
    private var tetris = ArrayList<ArrayList<color>>()

    private var pieceRotation= ArrayList<ArrayList<Array<Int>>>()
    private var actualColor = -1
    private var nextColor = -1

    private var colorList = ArrayList<Int>()
    private var endGame = true
    private var score = 0

    private var turned = 0
    private var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tetris)

        colorList.add(R.drawable.white)
        colorList.add(R.drawable.red)
        colorList.add(R.drawable.yellow)
        colorList.add(R.drawable.purple)
        colorList.add(R.drawable.lightblue)
        colorList.add(R.drawable.darkblue)
        colorList.add(R.drawable.green)


        initBoard()

        adapter = ColorAdapter(this, tetris, colorList)
        terrain.adapter = adapter

        Left.setOnClickListener {
            onMove(LEFT)
        }
        Right.setOnClickListener {
            onMove(RIGHT)
        }
        Rotate.setOnClickListener {
            onRotate()
        }
        NewGame.setOnClickListener {
            onNewGame()
        }
        startPlay()
    }

    private fun startPlay(){
        val alertDialog = AlertDialog.Builder(this@TetrisActivity)
        alertDialog.setTitle("Tetris")
        alertDialog.setMessage("Règles du tetris:\n" +
                "Le but du Tetris est d'aligné les pièces qui descendent en ligne pour gagner des points.\n" +
                "Plus il y a de ligne supprimé en même temps, meilleur est le score.\n" +
                "Si une pièce ne peut pas apparaître sur l'écran, la partie est perdue"
        )
        alertDialog.setNeutralButton("Commencer la partie"){_,_ ->
            onNewGame()
        }
        alertDialog.create().show()
    }


    private fun onNewGame(){
        initBoard()
        turned = 0
        score = 0
        nextColor = -1
        checkUpdates()
        val setText = getText(R.string.score).toString() + score
        Score.text = setText
        addColor()

        if(endGame){
            endGame = false
            moveDown()
        }
    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
    }

    private fun moveDown(){
        timer = Timer()
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    if (endGame){
                        timer.cancel()
                        return
                    }
                    runOnUiThread {
                        onMove(DOWN)
                    }
                }
            },0, 750
        )
    }

    private fun initBoard(){
        tetris.clear()
        for (i in 0..13){
            tetris.add(initBoardColumns())
        }
    }
    private fun initBoardColumns() : ArrayList<color>{
        val array = ArrayList<color>()
        for (i in 0..11){
            array.add(color(WHITE, false))
        }
        return array
    }

    private fun checkUpdates(){
        adapter.notifyDataSetChanged()
    }

    private fun finDePartie(){
        endGame = true
        timer.cancel()
        val alertDialog = AlertDialog.Builder(this@TetrisActivity)
        alertDialog.setTitle("Fin de la partie")
        alertDialog.setMessage("Score final: " + score.toString())
        alertDialog.setNeutralButton("Ok"){_,_ ->}
        alertDialog.create().show()
    }

    private fun onMove(direction: Array<Int>){
        if(!endGame) {
            val colorMoveable = getMoveableColor()
            var pieceCanMove = true
            if (colorMoveable.isNotEmpty()) {
                for (location in colorMoveable) {
                    if (!checkIfCanMove(direction, location)) {
                        pieceCanMove = false
                    }
                }
                if (pieceCanMove) {
                    val color = tetris[colorMoveable[0][0]][colorMoveable[0][1]].color
                    for (location in colorMoveable) {
                        tetris[location[0]][location[1]].color = WHITE
                        tetris[location[0]][location[1]].isMoveable = false
                    }
                    for (location in colorMoveable) {
                        tetris[location[0] + direction[0]][location[1] + direction[1]].color = color
                        tetris[location[0] + direction[0]][location[1] + direction[1]].isMoveable = true
                    }
                    for (piece in pieceRotation) {
                        for (carre in piece) {
                            carre[0] += direction[0]
                            carre[1] += direction[1]
                        }
                    }
                } else {
                    if (direction contentEquals DOWN) {
                        for (location in colorMoveable) {
                            tetris[location[0]][location[1]].isMoveable = false
                        }
                        checkLine()
                        addColor()
                    }
                }
            }
            checkUpdates()
        }
    }

    private fun onRotate(){
        val futureTurn =(turned+1)%4
        var checkRotate = true
        if (checkIfPieceInBoard(pieceRotation[futureTurn])){
            if(checkCollision(pieceRotation[futureTurn])){
                checkRotate = false
            }
        }else{
            checkRotate = false
        }
        if (checkRotate){
            removePiece(pieceRotation[turned])
            turned = (turned+1)%4
            addPiece(pieceRotation[turned])
            checkUpdates()
        }
    }

    private fun checkIfPieceInBoard(Piece: ArrayList<Array<Int>>): Boolean{
        for(carre in Piece){
            if(carre[0] < 0 || carre[0] >= tetris.size || carre[1]< 0 || carre[1] >= tetris[0].size){
                return false
            }
        }
        return true
    }

    private fun checkCollision(Piece: ArrayList<Array<Int>>) : Boolean{
        for(carre in Piece){
            if(tetris[carre[0]][carre[1]].color != WHITE && !tetris[carre[0]][carre[1]].isMoveable){
                return true
            }
        }
        return false
    }

    private fun addPiece(Piece: ArrayList<Array<Int>>){
        for (carre in Piece){
            tetris[carre[0]][carre[1]].color = actualColor
            tetris[carre[0]][carre[1]].isMoveable = true
        }
    }

    private fun removePiece(Piece: ArrayList<Array<Int>>){
        for (carre in Piece){
            tetris[carre[0]][carre[1]].color = WHITE
            tetris[carre[0]][carre[1]].isMoveable = false
        }
    }

    private fun checkLine(){
        var gain = 0
        var i = tetris.size-1
        while (i > 0){
            if(tetris[i].filter { it.color > WHITE }.size == 12){
                for(j in i downTo 1){
                    val array = ArrayList<color>()
                    for (k in 0..11){
                        val color= color(tetris[j-1][k].color,false)
                        array.add(color)
                    }
                    tetris[j] = array
                }
                gain++
            }else{
                i--
            }
        }
        score += gain*gain
        val setText = getText(R.string.score).toString() + score
        Score.text = setText
    }

    private fun getMoveableColor(): ArrayList<Array<Int>>{
        val colorMoveable = ArrayList<Array<Int>>()
        for(i in 0..13){
            for (j in 0..11){
                if (tetris[i][j].isMoveable){
                    colorMoveable.add(arrayOf(i,j))
                }
            }
        }
        return colorMoveable
    }

    private fun checkIfCanMove(movement: Array<Int>, location: Array<Int>) : Boolean{
        var canMove = false
        val newLocation = arrayOf(location[0]+movement[0],location[1]+movement[1])
        if(checkIfInBoard(newLocation)
            && (tetris[newLocation[0]][newLocation[1]].color == WHITE || tetris[newLocation[0]][newLocation[1]].isMoveable)){
            canMove = true
        }
        return canMove
    }

    private fun checkIfInBoard(location: Array<Int>): Boolean{
        return (location[0] >= 0 && location[0] < tetris.size && location[1] >= 0 && location[1] < tetris[0].size)
    }

    private fun addColor() {
        if(nextColor == -1) {
            nextColor = (1..(colorList.size - 1)).random()
        }
        pieceRotation.clear()
        val pieceRotation0 = ArrayList<Array<Int>>()
        val pieceRotation1= ArrayList<Array<Int>>()
        val pieceRotation2 = ArrayList<Array<Int>>()
        val pieceRotation3 = ArrayList<Array<Int>>()
        turned = 0
        when (nextColor) {
            1 -> {//RED:S
                pieceRotation0.add(arrayOf(0, 5))
                pieceRotation0.add(arrayOf(1, 5))
                pieceRotation0.add(arrayOf(1, 6))
                pieceRotation0.add(arrayOf(2, 6))

                pieceRotation1.add(arrayOf(1, 5))
                pieceRotation1.add(arrayOf(1, 6))
                pieceRotation1.add(arrayOf(0, 6))
                pieceRotation1.add(arrayOf(0, 7))

                pieceRotation2.add(arrayOf(0, 5))
                pieceRotation2.add(arrayOf(1, 5))
                pieceRotation2.add(arrayOf(1, 6))
                pieceRotation2.add(arrayOf(2, 6))

                pieceRotation3.add(arrayOf(0, 5))
                pieceRotation3.add(arrayOf(1, 5))
                pieceRotation3.add(arrayOf(1, 6))
                pieceRotation3.add(arrayOf(2, 6))
            }
            2 -> {//YELLOW:O
                pieceRotation0.add(arrayOf(0, 5))
                pieceRotation0.add(arrayOf(0, 6))
                pieceRotation0.add(arrayOf(1, 5))
                pieceRotation0.add(arrayOf(1, 6))

                pieceRotation1.add(arrayOf(0, 5))
                pieceRotation1.add(arrayOf(0, 6))
                pieceRotation1.add(arrayOf(1, 5))
                pieceRotation1.add(arrayOf(1, 6))

                pieceRotation2.add(arrayOf(0, 5))
                pieceRotation2.add(arrayOf(0, 6))
                pieceRotation2.add(arrayOf(1, 5))
                pieceRotation2.add(arrayOf(1, 6))

                pieceRotation3.add(arrayOf(0, 5))
                pieceRotation3.add(arrayOf(0, 6))
                pieceRotation3.add(arrayOf(1, 5))
                pieceRotation3.add(arrayOf(1, 6))
            }
            3 -> {//PURPLE:T
                pieceRotation0.add(arrayOf(1, 5))
                pieceRotation0.add(arrayOf(1, 6))
                pieceRotation0.add(arrayOf(1, 7))
                pieceRotation0.add(arrayOf(0, 6))

                pieceRotation1.add(arrayOf(0, 6))
                pieceRotation1.add(arrayOf(1, 6))
                pieceRotation1.add(arrayOf(2, 6))
                pieceRotation1.add(arrayOf(1, 7))

                pieceRotation2.add(arrayOf(1, 5))
                pieceRotation2.add(arrayOf(1, 6))
                pieceRotation2.add(arrayOf(1, 7))
                pieceRotation2.add(arrayOf(2, 6))

                pieceRotation3.add(arrayOf(0, 6))
                pieceRotation3.add(arrayOf(1, 6))
                pieceRotation3.add(arrayOf(2, 6))
                pieceRotation3.add(arrayOf(1, 5))
            }
            4 -> {//LIGHTBLUE:I
                pieceRotation0.add(arrayOf(0, 5))
                pieceRotation0.add(arrayOf(1, 5))
                pieceRotation0.add(arrayOf(2, 5))
                pieceRotation0.add(arrayOf(3, 5))

                pieceRotation1.add(arrayOf(2, 4))
                pieceRotation1.add(arrayOf(2, 5))
                pieceRotation1.add(arrayOf(2, 6))
                pieceRotation1.add(arrayOf(2, 7))

                pieceRotation2.add(arrayOf(1, 5))
                pieceRotation2.add(arrayOf(2, 5))
                pieceRotation2.add(arrayOf(3, 5))
                pieceRotation2.add(arrayOf(4, 5))

                pieceRotation3.add(arrayOf(2, 6))
                pieceRotation3.add(arrayOf(2, 5))
                pieceRotation3.add(arrayOf(2, 4))
                pieceRotation3.add(arrayOf(2, 3))
            }
            5 -> {//DARKBLUE:j
                pieceRotation0.add(arrayOf(0, 6))
                pieceRotation0.add(arrayOf(1, 6))
                pieceRotation0.add(arrayOf(2, 6))
                pieceRotation0.add(arrayOf(2, 5))

                pieceRotation1.add(arrayOf(1, 6))
                pieceRotation1.add(arrayOf(2, 6))
                pieceRotation1.add(arrayOf(2, 7))
                pieceRotation1.add(arrayOf(2, 8))

                pieceRotation2.add(arrayOf(2, 6))
                pieceRotation2.add(arrayOf(2, 7))
                pieceRotation2.add(arrayOf(3, 6))
                pieceRotation2.add(arrayOf(4, 6))

                pieceRotation3.add(arrayOf(2, 6))
                pieceRotation3.add(arrayOf(3, 6))
                pieceRotation3.add(arrayOf(2, 5))
                pieceRotation3.add(arrayOf(2, 4))
            }
            6 -> {//GREEN:L
                pieceRotation0.add(arrayOf(0, 5))
                pieceRotation0.add(arrayOf(1, 5))
                pieceRotation0.add(arrayOf(2, 5))
                pieceRotation0.add(arrayOf(2, 6))

                pieceRotation1.add(arrayOf(2, 5))
                pieceRotation1.add(arrayOf(2, 6))
                pieceRotation1.add(arrayOf(2, 7))
                pieceRotation1.add(arrayOf(3, 5))

                pieceRotation2.add(arrayOf(2, 4))
                pieceRotation2.add(arrayOf(2, 5))
                pieceRotation2.add(arrayOf(3, 5))
                pieceRotation2.add(arrayOf(4, 5))

                pieceRotation3.add(arrayOf(1, 5))
                pieceRotation3.add(arrayOf(2, 5))
                pieceRotation3.add(arrayOf(2, 4))
                pieceRotation3.add(arrayOf(2, 3))
            }
        }
        pieceRotation.add(pieceRotation0)
        pieceRotation.add(pieceRotation1)
        pieceRotation.add(pieceRotation2)
        pieceRotation.add(pieceRotation3)
        for (carre in pieceRotation[turned]) {
            if (tetris[carre[0]][carre[1]].color == WHITE) {
                tetris[carre[0]][carre[1]].color = nextColor
                tetris[carre[0]][carre[1]].isMoveable = true
            } else {
                finDePartie()
                return
            }
        }
        actualColor = nextColor
        nextColor = (1..(colorList.size - 1)).random()
        afficheNextPiece()
    }
    private fun afficheNextPiece() {
        when (nextColor) {
            1 -> {
                nextPiece.setImageResource(R.drawable.redpiece)
            }
            2 -> {
                nextPiece.setImageResource(R.drawable.yellowpiece)
            }
            3 -> {
                nextPiece.setImageResource(R.drawable.purplepiece)
            }
            4 -> {
                nextPiece.setImageResource(R.drawable.lightbluepiece)
            }
            5 -> {
                nextPiece.setImageResource(R.drawable.darkbluepiece)
            }
            6 -> {
                nextPiece.setImageResource(R.drawable.greenpiece)
            }
        }
    }
}
