package com.example.githubparser.utils.repositoryutils

import com.example.githubparser.Database.objectbox.ObjectBox
import com.example.githubparser.model.Repository
import com.example.githubparser.model.Stargazers
import io.objectbox.kotlin.boxFor

class RepositoryBase {
    private var repositoryBox = ObjectBox.boxStore.boxFor<Repository>()

    fun getRepositoriesList(): List<Repository> {
        return repositoryBox.query().build().find()
    }

    fun putRepository(repository: Repository) {
        repositoryBox.put(repository)
    }

    fun removeRepository(repository: Repository) {
        var stargazersBox = ObjectBox.boxStore.boxFor<Stargazers>()
        stargazersBox.query().build().find().forEach {
            if (it.id == repositoryBox.getId(repository))
            stargazersBox.query().build().find().remove(it)
        }
        repositoryBox.remove(repository)
    }

    fun compareRepositories(ownerText: String, repositoryText: String, dataBase: List<Repository>): Boolean {
        dataBase.forEach {
            if (it.ownerName == ownerText && it.repositoryName == repositoryText) {
                return false
            }
        }
        return true
    }

}