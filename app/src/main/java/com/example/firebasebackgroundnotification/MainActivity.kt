package com.example.firebasebackgroundnotification

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    lateinit var editText: EditText
    lateinit var button: Button
    lateinit var textView: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.numberEditText)
        button = findViewById(R.id.enterButtonId)
        textView = findViewById(R.id.textViewId)

        sharedPreferences = getSharedPreferences("UserNumber", MODE_PRIVATE)

        FirebaseApp.initializeApp(applicationContext)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
            if (task.isSuccessful) {
                val token = task.result

                Log.e("token.", token!!)
            }
        }
        button.setOnClickListener {
            saveNumberInSharedPreferences()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveNumberInSharedPreferences() {
        editor = sharedPreferences.edit()
        editor.putInt("Number", Integer.parseInt(editText.text.toString()))
        editor.apply()

        val text = editText.text.toString()
        textView.text = "Your number: $text"
        editText.setText("")
    }
}