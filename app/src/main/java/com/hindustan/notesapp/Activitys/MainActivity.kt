package com.hindustan.notesapp.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.hindustan.notesapp.Adapter.ToDoAdapter
import com.hindustan.notesapp.Database.Note
import com.hindustan.notesapp.Database.NoteDao
import com.hindustan.notesapp.Database.NoteDatabase
import com.hindustan.notesapp.R
import com.hindustan.notesapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var adapter:ToDoAdapter? =null
    var grid:Boolean = false
    var note:List<Note> = ArrayList()
    var noteDao:NoteDao? = null
    var database:NoteDatabase? = null
    companion object{
        var isDarkModeOn = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

         database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "notes"
        ).allowMainThreadQueries().build()

        // Obtain the NoteDao from the database

        val sharedPreferences = getSharedPreferences(
            "sharedPrefs", MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
         isDarkModeOn = sharedPreferences
            .getBoolean(
                "isDarkModeOn", false
            )

        if (isDarkModeOn) {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_YES);
            binding.ivMode.setImageResource(R.drawable.iv_light_mode)
        }
        else {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_NO);
            binding.ivMode.setImageResource(R.drawable.iv_dark_mode)
        }

        binding.ivMode.setOnClickListener {

            if (isDarkModeOn) {

                // if dark mode is on it
                // will turn it off
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_NO);
                // it will set isDarkModeOn
                // boolean to false
                editor.putBoolean(
                    "isDarkModeOn", false);
                editor.apply();

                binding.ivMode.setImageResource(R.drawable.iv_light_mode)

            }
            else {

                // if dark mode is off
                // it will turn it on
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_YES);

                // it will set isDarkModeOn
                // boolean to true
                editor.putBoolean(
                    "isDarkModeOn", true);
                editor.apply();

                // change text of Button
               binding.ivMode.setImageResource(R.drawable.iv_dark_mode)
            }

        }

        noteDao = database!!.noteDao()
        note = noteDao!!.getAllNotes()

        setupRecyclerView()

        binding.ivLayout.setOnClickListener {
            if (grid == false){
                grid = true
                binding.ivLayout.setImageResource(R.drawable.iv_list)
            }else{
                grid = false
                binding.ivLayout.setImageResource(R.drawable.iv_grid_view)
            }
            setupRecyclerView()
        }


        binding.addToDo.setOnClickListener {

            startActivity(Intent(this,AddToDoActivity::class.java))

        }



    }

    // Function to set up RecyclerView
    private fun setupRecyclerView() {
        noteDao = database!!.noteDao()
        note = noteDao!!.getAllNotes()

        if (!note.isNullOrEmpty()) {
            binding.ivEmpty.visibility = View.GONE
            binding.rvToDo.visibility = View.VISIBLE
            binding.rvToDo.setHasFixedSize(true)

            if (grid == true) {
                binding.rvToDo.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            } else {
                binding.rvToDo.layoutManager = LinearLayoutManager(this)
            }

            adapter = ToDoAdapter(note)
            binding.rvToDo.adapter = adapter
        } else {
            binding.ivEmpty.visibility = View.VISIBLE
            binding.rvToDo.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")
        setupRecyclerView()

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("TAG", "onRestart: ")
        setupRecyclerView()

    }

}