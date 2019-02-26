package fr.isen.toiletgame


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView








class MyAdapter(context: Context, tokens: Array<Int>) : ArrayAdapter<Int>(context, 0, tokens) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_my_adapter, parent, false)
        }

        var viewHolder = view()
        viewHolder.pawn = convertView!!.findViewById(R.id.pawn)

        val couleur= getItem(position)

        if(couleur == 0) {
            viewHolder.pawn!!.setImageResource(0)
        }
        if(couleur == 1){
            viewHolder.pawn!!.setImageResource(R.drawable.redpawn)
        }
        if(couleur == 2){
            viewHolder.pawn!!.setImageResource(R.drawable.yellowpawn)
        }


        return convertView
    }



    private inner class view {
        var pawn: ImageView? = null
    }
}