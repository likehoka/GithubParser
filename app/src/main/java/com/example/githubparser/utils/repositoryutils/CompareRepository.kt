package com.example.githubparser.utils.repositoryutils

import com.example.githubparser.model.Repository

class CompareRepository {
    fun compareDataBase(ownerText: String, repositoryText: String, dataBase: List<Repository>): Boolean {
        dataBase.forEach {
            if (it.ownerName == ownerText && it.repositoryName == repositoryText) {
                return false
            }
        }
        return true
    }
}