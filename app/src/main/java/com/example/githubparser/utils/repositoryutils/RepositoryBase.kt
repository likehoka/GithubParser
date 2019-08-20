package com.example.githubparser.utils.repositoryutils

import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Repository
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class RepositoryBase () {
    private var notesRepository = ObjectBox.boxStore.boxFor<Repository>()

    fun getDataBase(): List<Repository> {
        return notesRepository.query().build().find()
    }

    fun putDataBase(repository: Repository) {
        notesRepository.put(repository)
    }

    fun deleteData(repository: Repository) {
        var notesStargazers = ObjectBox.boxStore.boxFor<Stargazers>()
        notesStargazers.query().build().find().forEach {
            if (it.id == notesRepository.getId(repository))
            notesStargazers.query().build().find().remove(it)
        }
        notesRepository.remove(repository)
    }


}