package com.example.mint.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mint.R
import com.example.mint.adapters.BadgeAdapter
import com.example.mint.databinding.FragmentBadgesBinding
import com.example.mint.helpers.DbHelper

class BadgesFragment : Fragment() {
    private var _binding: FragmentBadgesBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DbHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBadgesBinding.inflate(inflater, container, false)
        dbHelper = DbHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userId = dbHelper.getUserId(prefs.getString("username", "") ?: "")

        if (userId != null) {
            val badges = dbHelper.getBadges(userId)
            if (badges.isNotEmpty()) {
                binding.badgeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.badgeRecyclerView.adapter = BadgeAdapter(badges)
                binding.noBadgesText.visibility = View.GONE
            } else {
                binding.noBadgesText.visibility = View.VISIBLE
            }
        } else {
            binding.noBadgesText.text = "No badges earned yet."
            binding.noBadgesText.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}