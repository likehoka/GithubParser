package com.example.githubparser.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.Activity.DetailsActivity
import com.example.githubparser.Model.Stargazers
import com.example.githubparser.R
import kotlinx.android.synthetic.main.layout_stargazers.view.*

class AdapterStargazers(val stargazers: List<Stargazers>) :
    RecyclerView.Adapter<AdapterStargazers.StargazersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StargazersViewHolder {
        return StargazersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_stargazers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StargazersViewHolder, position: Int) {
        val stargazer = stargazers[position]
        holder.view.starred_atTV.text = stargazer.starred_at
        holder.view.login.text = stargazer.user.username
        holder.view.btnDetailsActivity.setOnClickListener {
            val intentDetailsActivity = Intent(holder.view.context, DetailsActivity::class.java)
            intentDetailsActivity.putExtra("webUrl", stargazer.user.html_url)
            Log.d("mLog", "Передаем: ${stargazer.user.html_url}")
            holder.view.context.startActivity(intentDetailsActivity)
        }
    }

    override fun getItemCount() = stargazers.size
    class StargazersViewHolder(val view: View) : RecyclerView.ViewHolder(view)


}




