package fr.isen.toiletgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.image_tetris.view.*


class colorAdapter : BaseAdapter {
    var tetris : Array<Array<color>>
    var context: Context? = null
    var colorList : ArrayList<Int>


    constructor(context: Context, tetris: Array<Array<color>>, colorList: ArrayList<Int>) : super() {
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

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var colorView = inflator.inflate(R.layout.image_tetris, null)
        colorView.color.setImageResource(colorList[color])

        return colorView
    }
}