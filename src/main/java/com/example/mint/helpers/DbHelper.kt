package com.example.mint.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.mint.R
import com.example.mint.models.Badge
import com.example.mint.models.CategoryTotal
import com.example.mint.models.ExpenseItem
import com.example.mint.models.IncomeItem
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MintDatabase.db"
        private const val DATABASE_VERSION = 2

        // Users
        const val TABLE_USERS = "Users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        // Categories
        const val TABLE_CATEGORIES = "Categories"
        const val COLUMN_CATEGORY_ID = "id"
        const val COLUMN_CATEGORY_USER_ID = "user_id"
        const val COLUMN_CATEGORY_NAME = "name"
        const val COLUMN_CATEGORY_TYPE = "type"

        // Expenses
        const val TABLE_EXPENSES = "Expenses"
        const val COLUMN_EXPENSE_ID = "id"
        const val COLUMN_EXPENSE_USER_ID = "user_id"
        const val COLUMN_EXPENSE_NAME = "expense_name"
        const val COLUMN_EXPENSE_IMAGE_URI = "image_uri"
        const val COLUMN_EXPENSE_AMOUNT = "amount"
        const val COLUMN_EXPENSE_CATEGORY_ID = "category_id"
        const val COLUMN_EXPENSE_DATE = "date"
        const val COLUMN_EXPENSE_DESCRIPTION = "description"
        const val COLUMN_EXPENSE_START_TIME = "startTime"
        const val COLUMN_EXPENSE_END_TIME = "endTime"

        // Incomes
        const val TABLE_INCOMES = "Incomes"
        const val COLUMN_INCOME_ID = "id"
        const val COLUMN_INCOME_USER_ID = "user_id"
        const val COLUMN_INCOME_AMOUNT = "amount"
        const val COLUMN_INCOME_NAME = "income_name"
        const val COLUMN_INCOME_CATEGORY_ID = "category_id"
        const val COLUMN_INCOME_DATE = "date"
        const val COLUMN_INCOME_DESCRIPTION = "description"

        // Goals
        const val TABLE_GOALS = "Goals"
        const val COLUMN_GOAL_ID = "id"
        const val COLUMN_GOAL_USER_ID = "user_id"
        const val COLUMN_MIN_GOAL = "min_goal"
        const val COLUMN_MAX_GOAL = "max_goal"

        // Badges
        const val TABLE_BADGES = "Badges"
        const val COLUMN_BADGE_ID = "id"
        const val COLUMN_BADGE_NAME = "name"
        const val COLUMN_BADGE_DESCRIPTION = "description"
        const val COLUMN_BADGE_ICON_RES = "icon_resource"

        // Earned Badges
        const val TABLE_EARNED_BADGES = "EarnedBadges"
        const val COLUMN_EARNED_BADGE_ID = "badge_id"
        const val COLUMN_EARNED_BADGE_USER_ID = "user_id"
        const val COLUMN_EARNED_BADGE_NAME = "badge_name"
        const val COLUMN_EARNED_BADGE_DATE_AWARDED = "date_awarded"

        // Badges
        const val TABLE_MONTHLY_BUDGETS = "Badges"
        const val COLUMN_ID = "id"
        const val COLUMN_MONTHLY_BUDGET_USER_ID = "user_id"
        const val COLUMN_YEAR = "year"
        const val COLUMN_MONTH = "month"
        const val COLUMN_BUDGET_AMOUNT = "amount"
    }


    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD TEXT NOT NULL
            );
        """.trimIndent()

        val createCategoriesTable = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_USER_ID INTEGER,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL,
                $COLUMN_CATEGORY_TYPE TEXT NOT NULL,
                FOREIGN KEY($COLUMN_CATEGORY_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            );
        """.trimIndent()

        val createExpensesTable = """
            CREATE TABLE $TABLE_EXPENSES (
                $COLUMN_EXPENSE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EXPENSE_USER_ID INTEGER,
                $COLUMN_EXPENSE_NAME STRING NOT NULL,
                $COLUMN_EXPENSE_AMOUNT REAL NOT NULL,
                $COLUMN_EXPENSE_IMAGE_URI TEXT,
                $COLUMN_EXPENSE_CATEGORY_ID INTEGER NOT NULL,
                $COLUMN_EXPENSE_DATE TEXT NOT NULL,
                $COLUMN_EXPENSE_DESCRIPTION TEXT,
                $COLUMN_EXPENSE_START_TIME STRING,
                $COLUMN_EXPENSE_END_TIME STRING,
                FOREIGN KEY($COLUMN_EXPENSE_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                FOREIGN KEY($COLUMN_EXPENSE_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID)
            );
        """.trimIndent()

        val createIncomesTable = """
            CREATE TABLE $TABLE_INCOMES (
                $COLUMN_INCOME_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_INCOME_USER_ID INTEGER,
                $COLUMN_INCOME_AMOUNT REAL NOT NULL,
                $COLUMN_INCOME_NAME TEXT NOT NULL,
                $COLUMN_INCOME_CATEGORY_ID INTEGER NOT NULL,
                $COLUMN_INCOME_DATE TEXT NOT NULL,
                $COLUMN_INCOME_DESCRIPTION TEXT,
                FOREIGN KEY($COLUMN_INCOME_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                FOREIGN KEY($COLUMN_INCOME_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID)
            );
        """.trimIndent()

        val createGoalsTable = """
            CREATE TABLE $TABLE_GOALS (
                $COLUMN_GOAL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_GOAL_USER_ID INTEGER,
                $COLUMN_MIN_GOAL REAL,
                $COLUMN_MAX_GOAL REAL,
                FOREIGN KEY($COLUMN_GOAL_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            );
        """.trimIndent()

        val createMonthlyBudgetsTable = """ 
            CREATE TABLE $TABLE_MONTHLY_BUDGETS(
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_MONTHLY_BUDGET_USER_ID INTEGER UNIQUE,
            $COLUMN_YEAR INTEGER UNIQUE,
            $COLUMN_MONTH INTEGER UNIQUE,
            $COLUMN_BUDGET_AMOUNT REAL,
            );
            """.trimIndent()

        val createBadgesTable = """
    CREATE TABLE $TABLE_BADGES (
        $COLUMN_BADGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_BADGE_NAME TEXT UNIQUE,
        $COLUMN_BADGE_DESCRIPTION TEXT,
        $COLUMN_BADGE_ICON_RES INTEGER
    );
""".trimIndent()

// Earned Badges by Users
        val createEarnedBadgesTable = """
    CREATE TABLE $TABLE_EARNED_BADGES (
        $COLUMN_EARNED_BADGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_EARNED_BADGE_USER_ID INTEGER,
        $COLUMN_EARNED_BADGE_NAME TEXT UNIQUE,
        $COLUMN_EARNED_BADGE_DATE_AWARDED TEXT,
        UNIQUE($COLUMN_EARNED_BADGE_USER_ID, $COLUMN_EARNED_BADGE_ID),
        FOREIGN KEY($COLUMN_EARNED_BADGE_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
        FOREIGN KEY($COLUMN_EARNED_BADGE_ID) REFERENCES $TABLE_BADGES($COLUMN_BADGE_ID)
    );
""".trimIndent()

        val predefinedBadges = listOf(
            Badge("Expense Logger", "Logged expenses daily for 7 days", "TBD", R.drawable.expense_logger_icon),
            Badge("Budget Master", "Stayed within budget for 3 months", "TBD", R.drawable.budget_master_icon),
            Badge("Goal Getter", "Spent within set monthly goals", "TBD", R.drawable.goal_getter_icon)
        )

        db.execSQL(createUsersTable)
        db.execSQL(createCategoriesTable)
        db.execSQL(createExpensesTable)
        db.execSQL(createIncomesTable)
        db.execSQL(createGoalsTable)
        db.execSQL(createBadgesTable)
        db.execSQL(createEarnedBadgesTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INCOMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOALS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BADGES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EARNED_BADGES")
        onCreate(db)
    }
    fun insertUser(username: String, password: String): Long {
        val db = readableDatabase

        // Check for existing username
        val usernameCursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )
        if (usernameCursor.moveToFirst()) {
            usernameCursor.close()
            return -2L // Username exists
        }
        usernameCursor.close()


        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }

        return try {
            writableDatabase.insertOrThrow(TABLE_USERS, null, values)
        } catch (e: SQLException) {
            -1L // Unknown DB error
        }
    }
    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.query(
            TABLE_USERS,
            null,
            selection,
            arrayOf(username, password),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun checkUserExists(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getUserId(username: String): Int? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        val id =
            if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)) else null
        cursor.close()
        return id
    }

    fun insertCategory(userId: Int?, name: String, type: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            if (userId != null) put(COLUMN_CATEGORY_USER_ID, userId)
            put(COLUMN_CATEGORY_NAME, name)
            put(COLUMN_CATEGORY_TYPE, type)
        }
        return db.insert(TABLE_CATEGORIES, null, values)
    }

    fun getCategoryCountForUser(userId: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_CATEGORIES WHERE $COLUMN_CATEGORY_USER_ID = ?",
            arrayOf(userId.toString())
        )
        val count = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return count
    }

    fun getCategoryIdForName(name: String, userId: Int): Int? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CATEGORIES,
            arrayOf(COLUMN_CATEGORY_ID),
            "($COLUMN_CATEGORY_USER_ID IS NULL OR $COLUMN_CATEGORY_USER_ID = ?) AND $COLUMN_CATEGORY_NAME = ?",
            arrayOf(userId.toString(), name),
            null,
            null,
            null
        )
        val id = if (cursor.moveToFirst()) cursor.getInt(
            cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)
        ) else null
        cursor.close()
        return id
    }

    fun insertExpense(
        context: Context,
        userId: Int,
        amount: Double,
        categoryId: Int,
        date: String,
        description: String,
        expenseName: String,
        imageUri: String?,
        startTime: String,
        endTime: String
    ): Long {
        val db = writableDatabase

        // 1. Extract month and year from date
        val month = date.substring(5, 7)
        val year = date.substring(0, 4)

        // 2. Get total expenses so far for the month
        val cursor = db.rawQuery(
            """
        SELECT SUM(amount) FROM $TABLE_EXPENSES
        WHERE user_id = ? AND strftime('%m', date) = ? AND strftime('%Y', date) = ?
        """, arrayOf(userId.toString(), month, year)
        )
        val totalSoFar = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()

        // 3. Get max goal for user
        val goalCursor = db.rawQuery(
            "SELECT max_goal FROM Goals WHERE user_id = ?",
            arrayOf(userId.toString())
        )
        val maxGoal = if (goalCursor.moveToFirst()) goalCursor.getDouble(0) else null
        goalCursor.close()

        // 4. Check if expense exceeds max goal and show warning Toast
        if (maxGoal != null && totalSoFar + amount > maxGoal) {
            Toast.makeText(
                context,
                "⚠️ Warning: This expense exceeds your monthly spending goal!",
                Toast.LENGTH_LONG
            ).show()
            // Optionally return early to block insertion:
            // return -1L
        }

        // 5. Insert the expense
        val values = ContentValues().apply {
            put(COLUMN_EXPENSE_USER_ID, userId)
            put(COLUMN_EXPENSE_AMOUNT, amount)
            put(COLUMN_EXPENSE_CATEGORY_ID, categoryId)
            put(COLUMN_EXPENSE_DATE, date)
            put(COLUMN_EXPENSE_DESCRIPTION, description)
            put(COLUMN_EXPENSE_NAME, expenseName)
            put(COLUMN_EXPENSE_IMAGE_URI, imageUri)
            put(COLUMN_EXPENSE_START_TIME, startTime)
            put(COLUMN_EXPENSE_END_TIME, endTime)
        }

        Log.d("InsertExpense", "Inserting expense with date: $date")
        return db.insert(TABLE_EXPENSES, null, values)
    }

    fun getExpensesForUserInRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): List<ExpenseItem> {
        val db = readableDatabase

        // Log the start and end dates
        Log.d("ExpenseQuery", "Start Date: $startDate, End Date: $endDate")

        val query = """
        SELECT e.amount, c.name AS category, e.description, e.id AS expense_id, 
               e.expense_name, e.date, e.image_uri
        FROM $TABLE_EXPENSES e
        JOIN $TABLE_CATEGORIES c ON e.$COLUMN_EXPENSE_CATEGORY_ID = c.$COLUMN_CATEGORY_ID
        WHERE e.$COLUMN_EXPENSE_USER_ID = ? AND e.$COLUMN_EXPENSE_DATE BETWEEN ? AND ?
        ORDER BY e.$COLUMN_EXPENSE_DATE ASC
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString(), startDate, endDate))

        val expenses = mutableListOf<ExpenseItem>()
        if (cursor.moveToFirst()) {
            do {
                try {
                    val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                    val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    val expenseId = cursor.getInt(cursor.getColumnIndexOrThrow("expense_id"))
                    val expenseName = cursor.getString(cursor.getColumnIndexOrThrow("expense_name"))
                    val dateString = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                    val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_uri"))

                    // Log the raw date from the database
                    Log.d("ExpenseDate", "Stored date string: $dateString")

                    // Try parsing the date
                    val date: Date? = try {
                        val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
                        if (parsedDate != null && parsedDate.year > 100) {
                            parsedDate
                        } else {
                            Log.w("InvalidDate", "Skipping expense with invalid date: $dateString")
                            null
                        }
                    } catch (e: Exception) {
                        Log.e("DateParseError", "Failed to parse date: $dateString", e)
                        null
                    }

                    if (date != null) {
                        val expenseItem = ExpenseItem(
                            amount = amount,
                            category = category,
                            description = description,
                            expenseName = expenseName,
                            date = date,
                            imageUri = imageUri
                        )
                        expenses.add(expenseItem)
                    } else {
                        Log.w("ExpenseSkip", "Skipping expense with invalid date: $dateString")
                    }
                } catch (e: Exception) {
                    Log.e("ExpenseParseError", "Error parsing expense row", e)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return expenses
    }

    fun getExpensesForUser(userId: Int): List<ExpenseItem> {
        val expenseList = mutableListOf<ExpenseItem>()
        val db = readableDatabase

        val query = """
        SELECT e.amount, c.name AS category_name, e.description, e.expense_name, e.date, e.image_uri
        FROM $TABLE_EXPENSES e
        JOIN $TABLE_CATEGORIES c ON e.$COLUMN_EXPENSE_CATEGORY_ID = c.$COLUMN_CATEGORY_ID
        WHERE e.$COLUMN_EXPENSE_USER_ID = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category_name"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val expenseName = cursor.getString(cursor.getColumnIndexOrThrow("expense_name"))
                val dateString = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
                val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_uri"))


                val expenseItem = date?.let { ExpenseItem(amount, category, description, expenseName, it,imageUri) }
                if (expenseItem != null) {
                    expenseList.add(expenseItem)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return expenseList
    }

    fun insertIncome(
        userId: Int,
        amount: Double,
        categoryId: Int,
        date: String,
        description: String,
        incomeName: String
    ): Long {
        val db = writableDatabase

        // Store only the date part of the string (no timestamps)
        val formattedDate = date.substringBefore(" ")  // Or just use dateTime if it's already in the correct format

        val values = ContentValues().apply {
            put(COLUMN_INCOME_USER_ID, userId) // Correct field for user_id
            put(COLUMN_INCOME_AMOUNT, amount)
            put(COLUMN_INCOME_CATEGORY_ID, categoryId)
            put(COLUMN_INCOME_DATE, formattedDate) // Only store date (no time)
            put(COLUMN_INCOME_DESCRIPTION, description)
            put(COLUMN_INCOME_NAME, incomeName)
        }
        return db.insert(TABLE_INCOMES, null, values)
    }

    fun getCategoryNamesForUser(userId: Int): List<String> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_CATEGORY_NAME FROM $TABLE_CATEGORIES WHERE $COLUMN_CATEGORY_USER_ID = ?",
            arrayOf(userId.toString())
        )
        val categories = mutableListOf<String>()
        while (cursor.moveToNext()) {
            categories.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)))
        }
        cursor.close()
        return categories
    }

    fun saveSpendingGoals(userId: Int, minGoal: Double, maxGoal: Double): Long {
        val db = writableDatabase

        // Try update first
        val values = ContentValues().apply {
            put(COLUMN_MIN_GOAL, minGoal)
            put(COLUMN_MAX_GOAL, maxGoal)
        }
        val rowsUpdated = db.update(
            TABLE_GOALS,
            values,
            "$COLUMN_GOAL_USER_ID = ?",
            arrayOf(userId.toString())
        )

        if (rowsUpdated == 0) {
            // Insert if not existing
            values.put(COLUMN_GOAL_USER_ID, userId)
            return db.insert(TABLE_GOALS, null, values)
        }
        return rowsUpdated.toLong()
    }

    fun logAllExpenses() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, date FROM $TABLE_EXPENSES", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val date = cursor.getString(1)
            Log.d("DB_DEBUG", "Expense ID=$id | Date=$date")
        }
        cursor.close()
    }
    fun logAllIncomes() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, date FROM $TABLE_INCOMES", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val date = cursor.getString(1)
            Log.d("DB_DEBUG", "Income ID=$id | Date=$date")
        }
        cursor.close()
    }
    fun getIncomesForUser(userId: Int): List<IncomeItem> {
        val incomeList = mutableListOf<IncomeItem>()
        val db = readableDatabase

        // Query to join Incomes and Categories table to fetch income data for the user
        val query = """
            SELECT i.$COLUMN_INCOME_AMOUNT, 
                   c.$COLUMN_CATEGORY_NAME AS category, 
                   i.$COLUMN_INCOME_DESCRIPTION, 
                   i.$COLUMN_INCOME_NAME
            FROM $TABLE_INCOMES i
            JOIN $TABLE_CATEGORIES c ON i.$COLUMN_INCOME_CATEGORY_ID = c.$COLUMN_CATEGORY_ID
            WHERE i.$COLUMN_INCOME_USER_ID = ?
        """.trimIndent()

        // Debugging query output
        Log.d("DB_DEBUG", "Query: $query")

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        // Extract data from the cursor
        if (cursor.moveToFirst()) {
            do {
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INCOME_AMOUNT))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INCOME_DESCRIPTION))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INCOME_NAME))

                // Add the income item to the list
                incomeList.add(IncomeItem(amount, category, description, name))

                Log.d("DB_DEBUG", "Income: Amount=$amount | Category=$category | Description=$description | Name=$name")
            } while (cursor.moveToNext())
        }

        cursor.close()
        return incomeList
    }
    fun getTotalSpentByCategory(userId: Int, startDate: String, endDate: String): List<CategoryTotal> {
        val categoryTotals = mutableListOf<CategoryTotal>()
        val db = readableDatabase

        Log.d("SQL_DEBUG", "Querying totals for userId=$userId, startDate=$startDate, endDate=$endDate")

        val query = """
        SELECT c.$COLUMN_CATEGORY_NAME AS category_name, 
               SUM(e.$COLUMN_EXPENSE_AMOUNT) AS total_spent
        FROM $TABLE_EXPENSES e
        JOIN $TABLE_CATEGORIES c ON e.$COLUMN_EXPENSE_CATEGORY_ID = c.$COLUMN_CATEGORY_ID
        WHERE e.$COLUMN_EXPENSE_USER_ID = ? AND e.$COLUMN_EXPENSE_DATE BETWEEN ? AND ?
        GROUP BY c.$COLUMN_CATEGORY_NAME
    """.trimIndent()

        Log.d("SQL_DEBUG", "Running query:\n$query")

        val cursor = db.rawQuery(query, arrayOf(userId.toString(), startDate, endDate))

        Log.d("SQL_DEBUG", "Found ${cursor.count} rows")

        if (cursor.moveToFirst()) {
            do {
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category_name"))
                val total = cursor.getDouble(cursor.getColumnIndexOrThrow("total_spent"))

                Log.d("SQL_DEBUG", "Row: category=$category, total=$total")

                categoryTotals.add(CategoryTotal(category, total))
            } while (cursor.moveToNext())
        } else {
            Log.w("SQL_DEBUG", "No expense data found for user $userId in range $startDate to $endDate")
        }

        cursor.close()
        return categoryTotals
    }

    fun getSpendingGoals(userId: Int): Pair<Double, Double>? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT min_goal, max_goal FROM goals WHERE user_id = ?",
            arrayOf(userId.toString())
        )

        return if (cursor.moveToFirst()) {
            val min = cursor.getDouble(cursor.getColumnIndexOrThrow("min_goal"))
            val max = cursor.getDouble(cursor.getColumnIndexOrThrow("max_goal"))
            cursor.close()
            Pair(min, max)
        } else {
            cursor.close()
            null
        }
    }
    fun awardBadgeIfNotExists(userId: Int, badgeName: String): Boolean {
        val db = writableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM badges WHERE user_id = ? AND badge_name = ?",
            arrayOf(userId.toString(), badgeName)
        )

        return if (!cursor.moveToFirst()) {
            val values = ContentValues().apply {
                put("user_id", userId)
                put("badge_name", badgeName)
                put("date_awarded", SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()))
            }
            db.insert("badges", null, values)
            true
        } else {
            false
        }.also { cursor.close() }
    }

    fun getBadges(userId: Int): List<Badge> {
        val db = readableDatabase
        val badges = mutableListOf<Badge>()

        val query = """
        SELECT b.name, b.description, b.icon_resource, 
               (SELECT MIN(date_awarded) 
                FROM EarnedBadges e 
                WHERE e.badge_name = b.name AND e.user_id = ?) AS date_awarded
        FROM Badges b
        WHERE EXISTS (
            SELECT 1 FROM EarnedBadges e
            WHERE e.badge_name = b.name AND e.user_id = ?
        )
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString(), userId.toString()))

        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val description = cursor.getString(1)
            val iconResId = cursor.getInt(2)
            val dateAwarded = cursor.getString(3) ?: "Unknown date"

            badges.add(Badge(name, description, dateAwarded, iconResId, true))
        }

        cursor.close()
        return badges
    }


    fun getUserBadges(userId: Int): List<String> {
        val badges = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM Badges", arrayOf(userId.toString()))
        while (cursor.moveToNext()) {
            badges.add(cursor.getString(0))
        }
        cursor.close()
        return badges
    }
    fun insertBadge(userId: Int, badgeName: String, timestamp: String) {
        val db = writableDatabase
        val stmt = db.compileStatement("INSERT INTO Badges (name, timestamp) VALUES (?, ?, ?)")
        stmt.bindLong(1, userId.toLong())
        stmt.bindString(2, badgeName)
        stmt.bindString(3, timestamp)
        stmt.executeInsert()
    }
    fun loggedExpenseDailyFor(userId: Int, days: Int): Boolean {
        val db = readableDatabase
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days + 1)
        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)

        val cursor = db.rawQuery("""
        SELECT COUNT(DISTINCT date) 
        FROM Expenses 
        WHERE user_id = ? AND date >= ?
    """, arrayOf(userId.toString(), startDate))

        val result = cursor.moveToFirst() && cursor.getInt(0) == days
        cursor.close()
        return result
    }
    fun stayedWithinBudgetFor(userId: Int, months: Int): Boolean {
        val db = readableDatabase
        val calendar = Calendar.getInstance()

        // Fetch min and max goals once
        val goalsCursor = db.rawQuery("SELECT min_goal, max_goal FROM Goals WHERE user_id = ?", arrayOf(userId.toString()))
        if (!goalsCursor.moveToFirst()) {
            goalsCursor.close()
            return false // No goals set
        }
        val minGoal = goalsCursor.getDouble(0)
        val maxGoal = goalsCursor.getDouble(1)
        goalsCursor.close()

        var count = 0

        for (i in 0 until months) {
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

            val cursor = db.rawQuery("""
            SELECT SUM(amount) 
            FROM Expenses 
            WHERE user_id = ? AND strftime('%m', date) = ? AND strftime('%Y', date) = ?
        """, arrayOf(userId.toString(), "%02d".format(month), year.toString()))

            val totalSpent = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
            cursor.close()

            Log.d("BUDGET_GOAL_DEBUG", "[$month/$year] Total Spent: $totalSpent, Goal Range: $minGoal - $maxGoal")

            if (totalSpent in minGoal..maxGoal) {
                count++
            }

            calendar.add(Calendar.MONTH, -1)
        }

        Log.d("BUDGET_GOAL_DEBUG", "Months within goal: $count / Required: $months")
        return count == months
    }


    fun goalsMet(userId: Int): Boolean {
        val db = readableDatabase
        val sdf = SimpleDateFormat("yyyy-MM", Locale.US)
        val currentMonth = sdf.format(Date())

        val cursor = db.rawQuery("""
        SELECT SUM(amount) 
        FROM Expenses 
        WHERE user_id = ? AND strftime('%Y-%m', date) = ?
    """, arrayOf(userId.toString(), currentMonth))

        val totalSpent = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()

        val goalsCursor = db.rawQuery("SELECT min_goal, max_goal FROM Goals WHERE user_id = ?", arrayOf(userId.toString()))
        val met = if (goalsCursor.moveToFirst()) {
            val minGoal = goalsCursor.getDouble(0)
            val maxGoal = goalsCursor.getDouble(1)
            totalSpent in minGoal..maxGoal
        } else false
        goalsCursor.close()

        return met
    }

    private fun getMonthlyBudget(userId: Int): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT max_goal FROM Goals WHERE user_id = ?",
            arrayOf(userId.toString())
        )

        val maxGoal = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return maxGoal
    }
    fun insertDefaultBadgesForUser() {
        val db = writableDatabase

        val predefinedBadges = listOf(
            Triple("Expense Logger", "Logged expenses daily for 7 days", R.drawable.expense_logger_icon),
            Triple("Budget Master", "Stayed within budget for 3 months", R.drawable.budget_master_icon),
            Triple("Goal Getter", "Spent within set monthly goals", R.drawable.goal_getter_icon),
            Triple("First Step", "Logged first expense", R.drawable.first_step_icon),
            Triple("No Spend Start", "No spending for 3 consecutive days", R.drawable.star_icon)
        )

        for ((name, description, icon) in predefinedBadges) {
            val values = ContentValues().apply {
                put(COLUMN_BADGE_NAME, name)
                put(COLUMN_BADGE_DESCRIPTION, description)
                put(COLUMN_BADGE_ICON_RES, icon)
            }
            db.insertWithOnConflict(TABLE_BADGES, null, values, SQLiteDatabase.CONFLICT_IGNORE)
        }
    }

    private fun getBadgeAwardDate(userId: Int, badgeName: String): String {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT date_awarded FROM Badges WHERE user_id = ? AND badge_name = ?",
            arrayOf(userId.toString(), badgeName)
        )
        val date = if (cursor.moveToFirst()) cursor.getString(0) else "Unknown date"
        cursor.close()
        return date
    }

    fun getAllBadgesWithStatus(userId: Int): List<Badge> {
        val db = readableDatabase
        val allBadges = mutableListOf<Badge>()

        val query = """
        SELECT b.$COLUMN_BADGE_ID, b.$COLUMN_BADGE_NAME, b.$COLUMN_BADGE_DESCRIPTION, b.$COLUMN_BADGE_ICON_RES,
        (SELECT $COLUMN_EARNED_BADGE_DATE_AWARDED FROM $TABLE_EARNED_BADGES e 
         WHERE e.$COLUMN_EARNED_BADGE_USER_ID = ? AND e.$COLUMN_EARNED_BADGE_ID = b.$COLUMN_BADGE_ID) as date_awarded
        FROM $TABLE_BADGES b
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        while (cursor.moveToNext()) {
            val name = cursor.getString(1)
            val description = cursor.getString(2)
            val icon = cursor.getInt(3)
            val date = cursor.getString(4)
            val isEarned = date != null
            allBadges.add(Badge(name, description, date ?: "Not earned", icon, isEarned))
        }
        cursor.close()
        return allBadges
    }
    fun getEarnedBadges(userId: Int): List<Badge> {
        val db = readableDatabase
        val badgeList = mutableListOf<Badge>()

        val query = """
        SELECT b.$COLUMN_BADGE_NAME, b.$COLUMN_BADGE_DESCRIPTION, b.$COLUMN_BADGE_ICON_RES, e.$COLUMN_EARNED_BADGE_DATE_AWARDED
        FROM $TABLE_EARNED_BADGES e
        INNER JOIN $TABLE_BADGES b ON e.$COLUMN_EARNED_BADGE_ID = b.$COLUMN_BADGE_ID
        WHERE e.$COLUMN_EARNED_BADGE_USER_ID = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val description = cursor.getString(1)
            val icon = cursor.getInt(2)
            val dateAwarded = cursor.getString(3)
            badgeList.add(Badge(name, description, dateAwarded, icon, true))
        }
        cursor.close()
        return badgeList
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAndAwardBadges(userId: Int) {
        val earnedBadgeNames = getEarnedBadges(userId).map { it.name }

        //First Step Badge
        if (!earnedBadgeNames.contains("First Step") && getExpenseCount(userId) >= 1) {
            awardBadge(userId, "First Step")
        }

        //No Spend Star Badge
        if (!earnedBadgeNames.contains("No Spend Star") && hasNoSpendingStreak(userId, 3)) {
            awardBadge(userId, "No Spend Star")
        }

        //Expense Logger Badge
        if (!earnedBadgeNames.contains("Expense Logger") && loggedExpenseDailyFor(userId, 7)) {
            awardBadge(userId, "Expense Logger")
        }

        //Budget Master Badge
        if (!earnedBadgeNames.contains("Budget Master") && stayedWithinBudgetFor(userId, 3)) {
            awardBadge(userId, "Budget Master")
        }

        //Goal Getter Badge
        if (!earnedBadgeNames.contains("Goal Getter") && goalsMet(userId)) {
            awardBadge(userId, "Goal Getter")
        }
    }
    private fun awardBadge(userId: Int, badgeName: String) {
        val db = writableDatabase

        // Get the badge ID from the badge name
        val badgeIdQuery = "SELECT $COLUMN_BADGE_ID FROM $TABLE_BADGES WHERE $COLUMN_BADGE_NAME = ?"
        val cursor = db.rawQuery(badgeIdQuery, arrayOf(badgeName))

        if (cursor.moveToFirst()) {
            val badgeId = cursor.getInt(0)

            // Check if already awarded
            val checkQuery = """
            SELECT 1 FROM $TABLE_EARNED_BADGES 
            WHERE $COLUMN_EARNED_BADGE_USER_ID = ? AND $COLUMN_EARNED_BADGE_ID = ?
        """.trimIndent()
            val checkCursor = db.rawQuery(checkQuery, arrayOf(userId.toString(), badgeId.toString()))

            if (!checkCursor.moveToFirst()) {
                // Not awarded yet, so insert it
                val dateAwarded = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val values = ContentValues().apply {
                    put(COLUMN_EARNED_BADGE_USER_ID, userId)
                    put(COLUMN_EARNED_BADGE_ID, badgeId)
                    put(COLUMN_EARNED_BADGE_NAME, badgeName)
                    put(COLUMN_EARNED_BADGE_DATE_AWARDED, dateAwarded)
                }
                db.insert(TABLE_EARNED_BADGES, null, values)
            }
            checkCursor.close()
        }
        cursor.close()
    }

    private fun getExpenseCount(userId: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM Expenses WHERE user_id = ?",
            arrayOf(userId.toString())
        )
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun hasNoSpendingStreak(userId: Int, days: Int): Boolean {
        val db = readableDatabase
        val query = """
        SELECT DISTINCT date 
        FROM Expenses 
        WHERE user_id = ? 
        ORDER BY date DESC 
        LIMIT ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString(), (days + 1).toString()))

        val spentDates = mutableSetOf<String>()
        while (cursor.moveToNext()) {
            spentDates.add(cursor.getString(0))
        }
        cursor.close()

        val today = LocalDate.now()
        for (i in 1..days) {
            val date = today.minusDays(i.toLong()).toString()
            if (spentDates.contains(date)) return false
        }
        return true
    }

    fun setMonthlyBudget(userId: Int, year: Int, month: Int, amount: Double) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("year", year)
            put("month", month)
            put("budget_amount", amount)
        }

        // Insert or update
        db.insertWithOnConflict("MonthlyBudgets", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }
    fun getMonthlyBudget(userId: Int, month: Int, year: Int): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT budget_amount FROM MonthlyBudgets
        WHERE user_id = ? AND month = ? AND year = ?
        """.trimIndent(),
            arrayOf(userId.toString(), month.toString(), year.toString())
        )

        val budget = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return budget
    }

}