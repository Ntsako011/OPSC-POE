package com.example.mint.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mint.databinding.FragmentHomeBinding
import com.example.mint.helpers.DbHelper


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DbHelper
    private var userId: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize dbHelper once at the beginning
        dbHelper = DbHelper(requireContext())

        //Get current user session
        val sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        dbHelper.logAllExpenses()

        if (username != null) {
            userId = dbHelper.getUserId(username)

            if (userId != null) {
                val expenseList = dbHelper.getExpensesForUser(userId!!)
                val incomeList = dbHelper.getIncomesForUser(userId!!)

                val totalExpenseAmount = expenseList.sumOf { it.amount }
                binding.expensesBalanceTv.text = "R %.2f".format(totalExpenseAmount)

                val totalIncomeAmount = incomeList.sumOf { it.incomeAmount}
                binding.incomeBalanceTv.text = "R %.2f".format(totalIncomeAmount)



            } else {
                Toast.makeText(requireContext(), "User not found. Please log in again.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please log in again", Toast.LENGTH_SHORT).show()
        }
    }
}