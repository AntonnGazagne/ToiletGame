package fr.isen.toiletgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.image_tetris.view.*


class ColorAdapter : BaseAdapter {
    private var tetris : ArrayList<ArrayList<color>>
    private var context: Context? = null
    private var colorList : ArrayList<Int>


    constructor(context: Context, tetris: ArrayList<ArrayList<color>>, colorList: ArrayList<Int>) : super() {
        this.context = context
        this.tetris = tetris
        this.colorList = colorList
    }

    override fun getCount(): Int {
        return 14*12
    }

    override fun getItem(position: Int): Any {
        return tetris[position/12][position%12]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val color = this.tetris[position/12][position%12].color

        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val colorView = inflator.inflate(R.layout.image_tetris, null)
        colorView.color.setImageResource(colorList[color])

        return colorView
    }
}