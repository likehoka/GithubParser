package com.example.githubparser.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.model.Repository
import com.example.githubparser.R
import com.example.githubparser.activities.StargazersActivity
import kotlinx.android.synthetic.main.item_repository.view.*

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {
    //var itemRepositoryList: List<Repository> = emptyList()
    val repositoryList: MutableList<Repository> = mutableListOf()
    var itemRepositoryList: List<Repository> = repositoryList

    fun addItemRepository(repository: Repository) {
        itemRepositoryList = repositoryList
        repositoryList.add(0, repository)
        notifyItemInserted(0)
        Log.d("mLog", "itemcount = {$itemCount}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryAdapter.RepositoryViewHolder {
        return RepositoryAdapter.RepositoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repository, parent, false)
        )
    }
    override fun getItemCount(): Int = itemRepositoryList.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = itemRepositoryList[position]
        holder.itemView.ownerNameTextView.text = repository.ownerName
        holder.itemView.repositoryNameTextView.text = repository.repositoryName
        holder.itemView.repositoryCardView.setOnClickListener {
            val intentStargazersActivity = Intent(holder.itemView.context, StargazersActivity::class.java)
            intentStargazersActivity.putExtra("ownerName", repository.ownerName)
            intentStargazersActivity.putExtra("repositoryName", repository.repositoryName)
            holder.itemView.context.startActivity(intentStargazersActivity)
        }
    }
    class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}