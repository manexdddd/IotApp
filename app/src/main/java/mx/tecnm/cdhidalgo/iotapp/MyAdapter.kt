package mx.tecnm.cdhidalgo.iotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemAnimatorFinishedListener

class MyAdapter (private val dataset:Array<Array<String?>>,private val listener:itemListener):
    RecyclerView.Adapter<MyAdapter.ViewHolder>()
{
 class ViewHolder(v:View,listener:itemListener):RecyclerView.ViewHolder(v){
var tvItemId:TextView
var tvItemType:TextView
var tvItemValue:TextView
var tvItemName:TextView
var btnItemDate:TextView
var btnItemEdit:Button
var btnItemDelete:Button


                 init  {
                     tvItemId = v.findViewById(R.id.tvItemId)
                     tvItemType = v.findViewById(R.id.tvItemType)
                     tvItemValue = v.findViewById(R.id.tvItemValue)
                     tvItemName = v.findViewById(R.id.tvItemName)
                     btnItemEdit = v.findViewById(R.id.btnItemEdit)
                     btnItemDelete = v.findViewById(R.id.btnItemDelete)
                     btnItemDate = v.findViewById(R.id.btnItemDate)

                     btnItemEdit.setOnClickListener{
                        view ->listener.onEdit(view,adapterPosition)

                     }

                     btnItemDelete.setOnClickListener{
                             view ->listener.onDel(view,adapterPosition)

                     }

                     v.setOnClickListener { view-> listener.onClick(view,adapterPosition) }

                 }
 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sensor,parent,false)
        return  ViewHolder(v,listener)
    }

    override fun getItemCount(): Int {
     return dataset!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItemId.text = dataset!![position][0]
        holder.tvItemName.text = dataset!![position][1]
        holder.tvItemType.text = dataset!![position][2]
        holder.tvItemValue.text = dataset!![position][3]
        holder.btnItemDate.text = dataset!![position][4]

    }

}