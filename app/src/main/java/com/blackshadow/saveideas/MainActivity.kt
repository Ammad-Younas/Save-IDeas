package com.blackshadow.saveideas

import IdeasFragments
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var ideasFragment: IdeasFragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Save Ideas"
        supportActionBar?.subtitle = "Made by Ammad Younas"

        ideasFragment = IdeasFragments()
        supportFragmentManager.beginTransaction().add(R.id.fragementContainer, ideasFragment).commit()

        fab = findViewById(R.id.fab)
        fab.imageTintList = ContextCompat.getColorStateList(this, R.color.white)

        fab.setOnClickListener {
            showAddNewDialog()
        }
    }

    private fun showAddNewDialog() {
        val dialog = Dialog(this@MainActivity)
        val customDialogView = layoutInflater.inflate(R.layout.add_new_ideas, null)
        customDialogView.background = ContextCompat.getDrawable(this, R.drawable.custom_style_for_add_new_note)
        val titleInput = customDialogView.findViewById<EditText>(R.id.title)
        val descriptionInput = customDialogView.findViewById<EditText>(R.id.description)
        val addButton = customDialogView.findViewById<Button>(R.id.add)

        dialog.setContentView(customDialogView)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.apply {
            setLayout((resources.displayMetrics.widthPixels * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        }

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()
            if (title.isBlank()) {
                Toast.makeText(this@MainActivity, "Please enter title", Toast.LENGTH_SHORT).show()
            } else if (description.isBlank()) {
                Toast.makeText(this@MainActivity, "Please enter description", Toast.LENGTH_SHORT).show()
            } else {
                ideasFragment.addNewIdea(title, description)
                dialog.dismiss()
            }
        }
    }

    private fun fetchlist(): Boolean {
        val dbHelper = DatabaseHelper(this)
        val ideas = dbHelper.getAllIdeas()
        return ideas.isEmpty()
    }
}