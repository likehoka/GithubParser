package com.example.githubparser.utils.repositoryutils

import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Repository
import io.objectbox.kotlin.boxFor

class RepositoryBase () {
    private var notesRepository = ObjectBox.boxStore.boxFor<Repository>()

    fun getDataBase(): List<Repository> {
        return notesRepository.query().build().find()
    }

    fun putDataBase(repository: Repository) {
        notesRepository.put(repository)
    }
}