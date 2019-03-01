package fr.isen.toiletgame

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_tetris.*
import kotlinx.android.synthetic.main.image_tetris.view.*

class TetrisActivity : AppCompatActivity() {

    var adapter: colorAdapter? = null
    val color = color(0,false)
    var tetris =//14*12
        arrayOf(arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color),
            arrayOf(color,color,color,color,color,color,color,color,color,color,color,color)
        )


    var colorList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tetris)

        colorList.add(0)
        colorList.add(R.drawable.red)
        colorList.add(R.drawable.yellow)
        colorList.add(R.drawable.purple)
        colorList.add(R.drawable.lightblue)
        colorList.add(R.drawable.darkblue)
        colorList.add(R.drawable.green)

        adapter = colorAdapter(this, tetris, colorList)
        terrain.adapter = adapter

        Left.setOnClickListener {
            onMoveLeft()
        }
        Right.setOnClickListener {
            onMoveRigth()
        }
        RotateLeft.setOnClickListener {
            onRotateLeft()
        }
        RotateRight.setOnClickListener {
            onRotateRight()
        }
    }

    private fun onMoveLeft(){

    }

    private fun onMoveRigth(){

    }

    private fun onRotateLeft(){

    }

    private fun onRotateRight(){

    }

    private fun getMoveableColor(): ArrayList<Array<Int>>{
        var colorMoveable = ArrayList<Array<Int>>()
        for(i in 0..tetris.size){
            for (j in 0..tetris[0].size){
                if (tetris[i][j].isMoveable){
                    colorMoveable.add(arrayOf(i,j))
                }
            }
        }
        return colorMoveable
    }



}
