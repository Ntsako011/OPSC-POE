package com.example.mint.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mint.R
import com.example.mint.models.Badge

class BadgeAdapter(private val badges: List<Badge>) : RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.badgeIcon)
        val name: TextView = view.findViewById(R.id.badgeName)
        val description: TextView = view.findViewById(R.id.badgeDescription)
        val date: TextView = view.findViewById(R.id.badgeDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.badge_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val badge = badges[position]
        holder.icon.setImageResource(badge.iconResId)
        holder.name.text = badge.name
        holder.description.text = badge.description
        holder.date.text = badge.dateEarned
    }

    override fun getItemCount() = badges.size
}