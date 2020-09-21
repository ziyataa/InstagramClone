package com.example.instagramclone.model

class Comment {
    var comment: String = ""
    var publisher: String = ""

    constructor()

    constructor(comment: String, publisher: String) {
        this.comment = comment
        this.publisher = publisher
    }
}