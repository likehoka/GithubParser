package com.example.githubparser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubparser.R
import com.example.githubparser.model.Repository
import kotlinx.android.synthetic.main.item_repository.view.*

class RepositoryAdapter(
    private var callback: Callback? = null
) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    var repositoryList: List<Repository> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repository, parent, false)
        )
    }

    fun refreshAdapter() {notifyDataSetChanged()}

    override fun getItemCount(): Int = repositoryList.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repositoryList[position])
    }

    inner class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ownerNameTextView = itemView.findViewById<TextView>(R.id.ownerNameTextView)
        private val repositoryNameTextView = itemView.findViewById<TextView>(R.id.repositoryNameTextView)
        init {
            itemView.repositoryCardView.setOnClickListener {
                callback?.onOpenClicked(repositoryList[adapterPosition])
            }
            itemView.avatarRepository.setOnClickListener {
                callback?.onDeleteClicked(repositoryList[adapterPosition])
            }
        }

        fun bind(repository: Repository) {
            ownerNameTextView.text = repository.ownerName
            repositoryNameTextView.text = repository.repositoryName
        }
    }

    interface Callback {
        fun onDeleteClicked(repository: Repository)
        fun onOpenClicked(repository: Repository)
    }

}


