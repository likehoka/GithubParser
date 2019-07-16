package com.example.githubparser.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.activities.DetailsActivity
import com.example.githubparser.R
import com.example.githubparser.api.StargazersList
import kotlinx.android.synthetic.main.item_stargazers.view.*
import java.text.SimpleDateFormat
import java.util.*

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
        /*
        holder.view.btnDetailsActivity.setOnClickListener {
            //val intentDetailsActivity = Intent(holder.view.context, DetailsActivity::class.java)
            //intentDetailsActivity.putExtra("webUrl", stargazer.user.toString())
            Log.d("test", "Передаем: ${stargazer}")
            //holder.view.context.startActivity(intentDetailsActivity)
        }
        */
    }

    override fun getItemCount() = stargazers.size

    class StargazersViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}


/*
class StargazersAdapter(val stargazers: List<String>) :
    RecyclerView.Adapter<StargazersAdapter.StargazersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StargazersViewHolder {
        return StargazersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stargazers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StargazersViewHolder, position: Int) {

        //test
        val strDate: String = "2019-04-12T14:47:03Z"
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateStr: Date
        val stargazer = stargazers[position]
        dateStr = formatter.parse(stargazer.stringDate)

        holder.view.starred_atTV.text = "${dateStr.day }-${dateStr.month+1}-${dateStr.year+1900}"
        holder.view.login.text = stargazer.user.toString()
        holder.view.btnDetailsActivity.setOnClickListener {
            val intentDetailsActivity = Intent(holder.view.context, DetailsActivity::class.java)
            intentDetailsActivity.putExtra("webUrl", stargazer.user.toString())
            Log.d("test", "Передаем: ${stargazer.user.toString()}")
            holder.view.context.startActivity(intentDetailsActivity)
        }
    }

    override fun getItemCount() = stargazers.size

    class StargazersViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
*/



