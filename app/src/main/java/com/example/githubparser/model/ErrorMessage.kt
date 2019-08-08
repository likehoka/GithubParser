package com.example.githubparser.model

import com.google.gson.annotations.SerializedName


class ErrorMessage(
    @SerializedName("documentation_url")
    var documentationUrl: String = ""
)