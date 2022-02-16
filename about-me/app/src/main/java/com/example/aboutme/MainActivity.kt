package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myName: MyName = MyName("Neeraj")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //setContentView(R.layout.activity_main)
        binding.myName=myName
        binding.nickNameButton.setOnClickListener {
            addNickName(it)
        }
    }

    public fun addNickName(view: View) {
        //var editTextView = findViewById<EditText>(R.id.enter_nick_name)
        //var nickNameTextView = findViewById<TextView>(R.id.provided_name_name)
        binding.apply {
            myName?.nickname = enterNickName.text.toString()
            invalidateAll()
            enterNickName.visibility = View.GONE
            nickNameButton.visibility = View.GONE
            providedNameName.visibility = View.VISIBLE
        }
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}