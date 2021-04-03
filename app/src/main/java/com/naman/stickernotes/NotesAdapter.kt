package com.naman.stickernotes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naman.stickernotes.Data.Note

class NotesAdapter(private val mcontext: Context, private val listener:InNotesRvAdapter) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

val allNotes=ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val viewHolder=ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_note,parent,false))

        viewHolder.deleteButton.setOnClickListener {

            listener.onItemClicked(allNotes[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentNote=allNotes[position]
        holder.Text.setText(currentNote.text)


    }
    fun updateList( noteist:List<Note>){
        allNotes.clear()
        allNotes.addAll(noteist)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {

        return allNotes.size
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val Text=itemView.findViewById<TextView>(R.id.text)
        val deleteButton=itemView.findViewById<ImageView>(R.id.delete)

    }
    interface InNotesRvAdapter{
        fun onItemClicked(note:Note)
    }
}