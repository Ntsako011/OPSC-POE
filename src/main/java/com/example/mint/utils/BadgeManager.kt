package com.example.mint.utils

import android.content.Context
import android.widget.Toast
import com.example.mint.helpers.DbHelper
import java.text.SimpleDateFormat
import java.util.*

class BadgeManager(private val context: Context, private val dbHelper: DbHelper) {

    fun checkAndAwardBadges(userId: Int) {
        val earnedBadges = dbHelper.getUserBadges(userId)

        if (!earnedBadges.contains("Expense Logger") && dbHelper.loggedExpenseDailyFor(userId, 7)) {
            awardBadge(userId, "Expense Logger")
        }

        if (!earnedBadges.contains("Budget Master") && dbHelper.stayedWithinBudgetFor(userId, 3)) {
            awardBadge(userId, "Budget Master")
        }

        if (!earnedBadges.contains("Goal Getter") && dbHelper.goalsMet(userId)) {
            awardBadge(userId, "Goal Getter")
        }

    }

    private fun awardBadge(userId: Int, badgeName: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        dbHelper.insertBadge(userId, badgeName, timestamp)
        Toast.makeText(context, "🏆 You earned the '$badgeName' badge!", Toast.LENGTH_SHORT).show()
    }
}
