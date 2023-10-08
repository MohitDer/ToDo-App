package com.hindustan.notesapp.Activitys

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.hindustan.notesapp.Adapter.ToDoAdapter

import com.hindustan.notesapp.Database.Note
import com.hindustan.notesapp.Database.NoteDatabase
import com.hindustan.notesapp.R
import com.hindustan.notesapp.databinding.ActivityAddToDoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.math.log


class AddToDoActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddToDoBinding
    var date:String? = null
    var priority:Int = 0
    var id:Long = 0
    var adapter:ToDoAdapter? = null
    var noteList:List<Note> = ArrayList()


    @SuppressLint("SuspiciousIndentation", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_to_do)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val intent = intent

        id = intent.getLongExtra("id",0)


        // For Select Priority
        binding.rgPriority.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_Low -> {
                    priority = 0
                    Log.d("TAG", "onCreate: "+priority)
                }
                R.id.rb_Medium -> {
                    priority = 1
                    Log.d("TAG", "onCreate: "+priority)
                }
                R.id.rb_High -> {
                    priority = 2
                    Log.d("TAG", "onCreate: "+priority)
                }
                R.id.rb_Urgent -> {
                    priority = 3
                    Log.d("TAG", "onCreate: "+priority)
                }
            }
        }


        //For Dark Mode
        val sharedPreferences = getSharedPreferences(
            "sharedPrefs", MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        MainActivity.isDarkModeOn = sharedPreferences
            .getBoolean(
                "isDarkModeOn", false
            )

        if (MainActivity.isDarkModeOn) {
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

            if (MainActivity.isDarkModeOn) {

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

        } // <-- End Dark Mode

        val database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "notes"
        ).allowMainThreadQueries().build()

        // Obtain the NoteDao from the database
        val noteDao = database.noteDao()
        noteList = noteDao.getAllNotes()


        // For Save Data In Room Database

        binding.ivSave.setOnClickListener {
            val title:String = binding.etTitle.text.toString()
            val content:String = binding.etDiscription.text.toString()


                if (!title.isNullOrEmpty() && !content.isNullOrEmpty() && !date.isNullOrEmpty()) {
                    val note = Note(
                        title = title,
                        content = content,
                        priority = priority,
                        date = date!!
                    )



                    // Launch a coroutine to insert the note
                    GlobalScope.launch(Dispatchers.IO) {

                        if (!noteList.isNullOrEmpty()){
                            for (i in noteList) {
                                if (i.id == id) {
                                    Log.d("TAG", "onCreate: " + i.id + "=" + id)
                                    noteDao.update(note)
                                    break
                                } else {
                                    noteDao.insert(note)
                                    adapter = ToDoAdapter(noteDao.getAllNotes())
                                    adapter!!.notifyDataSetChanged()
                                }
                            }
                        }else{

                            noteDao.insert(note)
                            adapter = ToDoAdapter(noteDao.getAllNotes())
                            adapter!!.notifyDataSetChanged()
                        }



                    }

                    Toast.makeText(this@AddToDoActivity,"Task Saved",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Some Field Are Blank",Toast.LENGTH_SHORT).show()
                }
        } //<-- End Save Data in RoomDataBase

        binding.ivReset.setOnClickListener {

            binding.etTitle.setText("")
            binding.etDiscription.setText("")
            binding.tvDate.setText("Date")

        }

        // Date Piker
        binding.tvDatePiker.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                binding.tvDate.setText("" + dayOfMonth + "/ " + monthOfYear + "/ " + year)

                date = "" + dayOfMonth + "/ " + monthOfYear + "/ " + year

            }, year, month, day)

            dpd.show()
        } //<-- End date piker


    }

}



