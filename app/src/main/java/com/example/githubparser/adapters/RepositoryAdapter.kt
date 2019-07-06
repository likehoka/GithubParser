package com.example.githubparser.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.model.Repository
import com.example.githubparser.R
import com.example.githubparser.activities.GraphActivity
import kotlinx.android.synthetic.main.item_repository.view.*

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {
    private val repositoryList: MutableList<Repository> = mutableListOf()

    fun addItemRepository(repository: Repository) {
        repositoryList.add(0, repository)
        notifyItemInserted(0)
        Log.d("test", "itemcount = {$itemCount}")
    }

    fun addAllItemRepository(notes: MutableList<Repository>) {
        repositoryList.addAll(notes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryAdapter.RepositoryViewHolder {
        return RepositoryAdapter.RepositoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repository, parent, false)
        )
    }

    override fun getItemCount(): Int = repositoryList.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositoryList[position]
        holder.itemView.ownerNameTextView.text = repository.ownerName
        holder.itemView.repositoryNameTextView.text = repository.repositoryName
        holder.itemView.repositoryCardView.setOnClickListener {

            holder.itemView.context.startActivity(GraphActivity.createIntent(holder.itemView.context, repository.ownerName, repository.repositoryName))
        }
    }
    class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
}