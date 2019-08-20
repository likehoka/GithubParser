package com.example.githubparser.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.activities.DetailsActivity
import com.example.githubparser.R
import kotlinx.android.synthetic.main.item_stargazers.view.*

class StargazersAdapter(val stargazers: List<String>) :
    RecyclerView.Adapter<StargazersAdapter.StargazersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StargazersViewHolder {
        return StargazersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stargazers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StargazersViewHolder, position: Int) {

        val stargazer = stargazers[position]
        holder.view.login.text = stargazer
        holder.view.setOnClickListener {
            Log.d("test", "User: " + stargazer)
            val intentDetailsActivity = Intent(holder.view.context, DetailsActivity::class.java)
            intentDetailsActivity.putExtra("webUrl", stargazer)
            holder.view.context.startActivity(intentDetailsActivity)
        }
    }

    override fun getItemCount() = stargazers.size

    class StargazersViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}





