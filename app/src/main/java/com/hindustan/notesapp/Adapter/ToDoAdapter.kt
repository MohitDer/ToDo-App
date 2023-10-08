package com.hindustan.notesapp.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hindustan.notesapp.Activitys.AddToDoActivity
import com.hindustan.notesapp.Database.Note
import com.hindustan.notesapp.R

class ToDoAdapter(note: List<Note>) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    var note:List<Note>? = note


    class ViewHolder(item:View): RecyclerView.ViewHolder(item) {

        val tv_title = item.findViewById<TextView>(R.id.tv_ToDoTitle);
        val tv_content = item.findViewById<TextView>(R.id.tv_ToDoContent);
        val tv_date = item.findViewById<TextView>(R.id.tv_Date);
        val tv_importance = item.findViewById<TextView>(R.id.tv_importance);
        val cv_todo = item.findViewById<MaterialCardView>(R.id.cv_todo)





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoAdapter.ViewHolder {
       val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_to_do,parent,false)

        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ToDoAdapter.ViewHolder, position: Int) {

        holder.tv_title.text = note!!.get(position).title
        holder.tv_content.text = note!!.get(position).content
        holder.tv_date.text = note!!.get(position).date


        if (note!!.get(position).priority == 0){

            holder.tv_importance.text = "Low"
            holder.cv_todo.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.low))

        }else if (note!!.get(position).priority == 1){
            holder.tv_importance.text = "Medium"
            holder.cv_todo.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.medium))

        }else if (note!!.get(position).priority == 2){
            holder.tv_importance.text = "High"
            holder.cv_todo.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.high))


        }else if (note!!.get(position).priority == 3){
            holder.tv_importance.text = "Urgent"
            holder.cv_todo.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.urgent))
        }

        holder.cv_todo.setOnClickListener {

            val intent = Intent(holder.itemView.context,AddToDoActivity::class.java)
            intent.putExtra("id",note!!.get(position).id)
            holder.itemView.context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return note!!.size
    }
}