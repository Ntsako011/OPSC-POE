package com.example.mint.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mint.R
import com.example.mint.activities.CreateExpensesActivity
import com.example.mint.adapters.CategoryAdapter
import com.example.mint.databinding.FragmentCategoriesBinding
import com.example.mint.helpers.DbHelper
import com.example.mint.models.CategoryTotal
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.Calendar
import java.util.Locale

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DbHelper
    private lateinit var adapter: CategoryAdapter

    private var startDate: String = ""
    private var endDate: String = ""
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DbHelper(requireContext())

        val sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            userId = dbHelper.getUserId(username)

            if (userId != null) {
                // 🔹 Get goals and show them
                val goals = dbHelper.getSpendingGoals(userId!!)
                if (goals != null) {
                    val (minGoal, maxGoal) = goals
                    binding.minGoalTextView.text = "Min Goal: R %.2f".format(minGoal)
                    binding.maxGoalTextView.text = "Max Goal: R %.2f".format(maxGoal)
                }

                // 🔹 Show all-time category totals
                val categoryTotals = dbHelper.getTotalSpentByCategory(userId!!, "0000-01-01", "9999-12-31")
                displayCategoryTotals(categoryTotals)

                binding.redirectButton.setOnClickListener {
                    startActivity(Intent(requireContext(), CreateExpensesActivity::class.java))
                }
            } else {
                Toast.makeText(requireContext(), "User not found. Please log in again.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please log in again", Toast.LENGTH_SHORT).show()
        }

        binding.startDateButton.setOnClickListener {
            showDatePicker { date ->
                startDate = date
                binding.startDateButton.text = "Start: $date"
            }
        }

        binding.endDateButton.setOnClickListener {
            showDatePicker { date ->
                endDate = date
                binding.endDateButton.text = "End: $date"
            }
        }

        binding.filterCategoriesButton.setOnClickListener {
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                userId?.let { id ->
                    val filteredTotals = dbHelper.getTotalSpentByCategory(id, startDate, endDate)
                    displayCategoryTotals(filteredTotals)
                } ?: Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Select both dates", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayCategoryTotals(categoryTotals: List<CategoryTotal>) {
        val totalAmount = categoryTotals.sumOf { it.totalSpent }
        binding.totalAmtTv.text = "R %.2f".format(totalAmount)

        // RecyclerView setup
        adapter = CategoryAdapter(categoryTotals)
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.categoriesRecyclerView.adapter = adapter

        // Pie chart entries
        val pieEntries = categoryTotals.map {
            PieEntry(it.totalSpent.toFloat(), it.categoryName)
        }

        val dataSet = PieDataSet(pieEntries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        val pieData = PieData(dataSet)

        binding.categoryPieChart.apply {
            data = pieData
            setUsePercentValues(true)
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            centerText = "Spending by Category"
            setCenterTextSize(16f)
            legend.textColor = Color.WHITE
            description.isEnabled = false
            animateY(1000, Easing.EaseInOutQuad)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}