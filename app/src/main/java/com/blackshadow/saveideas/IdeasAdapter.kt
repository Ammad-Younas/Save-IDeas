package com.blackshadow.saveideas

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class IdeasAdapter(
    private val ideasList: MutableList<IdeasModel>,
    private val context: Context
) : RecyclerView.Adapter<IdeasAdapter.IdeasViewHolder>() {

    class IdeasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.idea)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)
        val card: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_for_recyclerview, parent, false)
        return IdeasViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdeasViewHolder, position: Int) {
        val idea = ideasList[position]
        holder.title.text = idea.title
        holder.description.text = idea.description
        holder.deleteIcon.setOnClickListener {
            deleteIdea(position)
        }

        holder.card.setOnClickListener {
            val dialog = Dialog(context)
            val customDialogView = LayoutInflater.from(context).inflate(R.layout.add_new_ideas, null)
            customDialogView.background = ContextCompat.getDrawable(context, R.drawable.custom_style_for_add_new_note)
            val titleInput = customDialogView.findViewById<TextView>(R.id.title)
            val descriptionInput = customDialogView.findViewById<TextView>(R.id.description)
            val addButton = customDialogView.findViewById<TextView>(R.id.add)
            val popupTitle = customDialogView.findViewById<TextView>(R.id.title_of_popup)

            titleInput.text = idea.title
            descriptionInput.text = idea.description
            addButton.text = "Update Idea"
            popupTitle.text = "Update Idea"

            dialog.setContentView(customDialogView)
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window?.apply {
                setLayout(
                    (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }

            addButton.setOnClickListener {
                val dbHelper = DatabaseHelper(context)
                val newTitle = titleInput.text.toString()
                val newDescription = descriptionInput.text.toString()

                val updatedRows = dbHelper.updateIdea(idea.id, newTitle, newDescription)
                if (updatedRows > 0) {
                    idea.title = newTitle
                    idea.description = newDescription
                    notifyItemChanged(position)
                    dialog.dismiss()
                }
            }
        }

        holder.card.setOnLongClickListener {
            deleteIdea(position)
            true
        }
    }

    private fun deleteIdea(position: Int) {
        val idea = ideasList[position]
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Delete Idea")
        dialogBuilder.setMessage("Are you sure you want to delete this idea?")
        dialogBuilder.setPositiveButton("Yes") { _, _ ->
            val dbHelper = DatabaseHelper(context)
            dbHelper.deleteIdea(idea.id)
            ideasList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, ideasList.size)
        }
        dialogBuilder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.setOnShowListener {
            alertDialog.window?.setBackgroundDrawableResource(R.color.blue)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(context.getColor(R.color.white))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(context.getColor(R.color.white))
        }
        alertDialog.show()
    }

    override fun getItemCount(): Int = ideasList.size
}