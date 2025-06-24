package com.example.mint.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mint.R
import com.example.mint.databinding.ActivityMainBinding
import com.example.mint.fragments.CategoriesFragment
import com.example.mint.fragments.ExpensesFragment
import com.example.mint.fragments.HomeFragment
import com.example.mint.fragments.IncomesFragment
import com.example.mint.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set initial fragment
        replaceFragment(HomeFragment())

        // Handle bottom nav item clicks
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeMn -> replaceFragment(HomeFragment())
                R.id.expensesMn -> replaceFragment(ExpensesFragment())
                R.id.incomesMn -> replaceFragment(IncomesFragment())
                R.id.categoriesMn -> replaceFragment(CategoriesFragment())
                R.id.settingsMn -> replaceFragment(SettingsFragment())
                else -> return@setOnItemSelectedListener false
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}