package com.swayy.kaba_connect.model

class Fara {

    private var post: String = ""
    private var publisher: String = ""

    constructor()

    constructor(post: String, publisher: String) {
        this.post = post
        this.publisher = publisher
    }

    fun getPost(): String{
        return post
    }

    fun getPublisher(): String{
        return publisher
    }

    fun setPost(post: String){
        this.post = post
    }

    fun setPublisher(publisher: String){
        this.publisher = publisher
    }
}