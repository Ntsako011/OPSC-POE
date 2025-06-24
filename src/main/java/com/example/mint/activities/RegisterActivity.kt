package com.example.mint.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mint.databinding.ActivityRegisterBinding
import com.example.mint.helpers.DbHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private  lateinit var databaseHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DbHelper(this)

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEt.text.toString().trim()
            val password = binding.registerPasswordEt.text.toString().trim()


            registerToDatabase(username,password)

        }
        binding.loginRedirectText.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun registerToDatabase(username: String, password: String ) {
        val insertRowId = databaseHelper.insertUser(username, password )
        if (insertRowId != -1L) {
            Toast.makeText(this, "Registration Successful!!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Check if user already exists
            if (databaseHelper.readUser(username, password)) {
                Toast.makeText(this, "Registration Failed: Username already exists!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registration Failed: Unknown error.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}