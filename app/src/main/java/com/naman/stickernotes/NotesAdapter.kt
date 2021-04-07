package com.naman.stickernotes

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naman.stickernotes.Data.Note
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_note.view.*

class NotesAdapter(private val mcontext: Context, private val listener: InNotesRvAdapter, s: String) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

private val allNotes=ArrayList<Note>()
    private val comingFrom:String=s
private var previousClicked:Int=-1
    private  lateinit var previousView:View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val viewHolder=ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_note,parent,false))

        if(comingFrom.equals("from main")){
            viewHolder.deleteButton.visibility=View.VISIBLE

        }


        viewHolder.deleteButton.setOnClickListener {
            listener.onItemClicked(allNotes[viewHolder.adapterPosition],"for_delete")

        }

        viewHolder.itemView.setOnClickListener {


            if(comingFrom != "from main") {
                if(previousClicked!=viewHolder.adapterPosition) {


                    viewHolder.check.visibility = View.VISIBLE
                    if(previousClicked!=-1){
                        previousView.check.visibility=View.GONE
                    }
                    previousClicked = viewHolder.adapterPosition
                    previousView=it
                    listener.onItemClicked(allNotes[viewHolder.adapterPosition],"get_checked")


                }else{

                    previousClicked=-1
                    viewHolder.check.visibility = View.GONE
                    listener.onItemClicked(null,"none")

                }
            }else{
                listener.onItemClicked(allNotes[viewHolder.adapterPosition],"intent_action")

            }
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

        val Text: TextView =itemView.findViewById(R.id.text)
        val deleteButton: ImageView =itemView.findViewById(R.id.delete)
        val check: ImageView =itemView.findViewById(R.id.check)


    }
    interface InNotesRvAdapter{

        fun onItemClicked(note: Note?,action:String)
    }
}