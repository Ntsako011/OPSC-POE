package com.example.mint.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mint.adapters.BadgeAdapter
import com.example.mint.databinding.ActivityBadgeBinding
import com.example.mint.helpers.DbHelper

class BadgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBadgeBinding
    private lateinit var dbHelper: DbHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBadgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        // Configure back button
        binding.backButton.setOnClickListener {
            finish() // closes this activity and returns to previous screen
        }

        // Step 1: Get logged-in user's ID from SharedPreferences
        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "") ?: ""
        val userId = dbHelper.getUserId(username)

        if (userId != null) {
            //Check if any badges should be awarded before showing
            dbHelper.checkAndAwardBadges(userId)

            // Refresh badge lists
            val earnedBadges = dbHelper.getEarnedBadges(userId)
            val allBadges = dbHelper.getAllBadgesWithStatus(userId)

            // Show earned badges
            if (earnedBadges.isNotEmpty()) {
                binding.noBadgesText.visibility = View.GONE
                binding.earnedBadgeRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.earnedBadgeRecyclerView.adapter = BadgeAdapter(earnedBadges)
            } else {
                binding.noBadgesText.text = "No badges earned yet."
                binding.noBadgesText.visibility = View.VISIBLE
            }

            // Show all badges
            binding.allBadgeRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.allBadgeRecyclerView.adapter = BadgeAdapter(allBadges)
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }
}