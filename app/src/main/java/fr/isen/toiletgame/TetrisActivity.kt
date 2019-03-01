package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_tetris.*

private val LEFT = arrayOf(0,-1)
private val RIGHT = arrayOf(0,1)
private val DOWN = arrayOf(1,0)

class TetrisActivity : AppCompatActivity() {

    private lateinit var adapter: colorAdapter
    private var tetris = ArrayList<ArrayList<color>>()


    private var colorList = ArrayList<Int>()
    private var endGame = false
    private var score = 0

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

        adapter = colorAdapter(this, tetris, colorList)
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
        onNewGame()

        moveDown()
    }

    private fun onNewGame(){
        tetris.clear()
        initBoard()
        endGame = false
        score = 0
        checkUpdates()
        onPlay()
    }

    private fun onPlay(){
        addColor(1)
    }

    private fun moveDown(){
        Handler().postDelayed({
            onMove(DOWN)
            moveDown()
        }, 500)
    }

    private fun initBoard(){
        for (i in 0..13){
            tetris.add(initBoardColumns())
        }
    }
    private fun initBoardColumns() : ArrayList<color>{
        val array = ArrayList<color>()
        for (i in 0..11){
            array.add(color(0, false))
        }
        return array
    }

    private fun checkUpdates(){
        adapter.notifyDataSetChanged()
    }

    private fun onMove(direction: Array<Int>){
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
                for (location in colorMoveable) {//On enlève la pièce
                    tetris[location[0]][location[1]].color = 0
                    tetris[location[0]][location[1]].isMoveable = false
                }
                for (location in colorMoveable) {
                    tetris[location[0]+direction[0]][location[1]+direction[1]].color = color
                    tetris[location[0]+direction[0]][location[1]+direction[1]].isMoveable = true
                }
            }else{
                if (direction contentEquals DOWN){
                    for (location in colorMoveable){
                        tetris[location[0]][location[1]].isMoveable = false
                    }
                    addColor((1..colorList.size-1).random())
                    checkLine()
                }
            }
        }
        checkUpdates()
    }

    private fun onRotate(){

    }

    private fun checkLine(){
        for (i in tetris.size-1 downTo 0){
            if(tetris[i].filter { it.color > 0 }.size == 12){
                for(j in i downTo 1){
                    tetris[i] = tetris[i-1]
                }
            }
        }
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
            && (tetris[newLocation[0]][newLocation[1]].color == 0 || tetris[newLocation[0]][newLocation[1]].isMoveable)){
            canMove = true
        }
        return canMove
    }

    private fun checkIfInBoard(location: Array<Int>): Boolean{
        return (location[0] >= 0 && location[0] < tetris.size && location[1] >= 0 && location[1] < tetris[0].size)
    }

    private fun addColor(color: Int){
        if(tetris[0][5].color == 0){
            tetris[0][5].color = color
            tetris[0][5].isMoveable = true
        }
    }
}
