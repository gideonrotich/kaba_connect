package com.swayy.kaba_connect.model

class Forum {
    private var name: String = ""
    private var description: String = ""
    private var image: String = ""
    private var publisher: String = ""
    private var forumId: String = ""


    constructor()
    constructor(name: String, description: String, image: String,publisher: String,forumId: String) {
        this.name = name
        this.description = description
        this.image = image
        this.publisher = publisher
        this.forumId = forumId
    }

    fun getName(): String
    {
        return name
    }

    fun setName(name: String)
    {
        this.name = name
    }

    fun getDescription(): String
    {
        return description
    }

    fun setDescription(description: String)
    {
        this.description = description
    }

    fun getImage(): String
    {
        return image
    }

    fun setImage(image: String)
    {
        this.image = image
    }

    fun getPublisher(): String
    {
        return publisher
    }

    fun setPublisher(publisher: String)
    {
        this.publisher = publisher
    }
    fun getForumId(): String
    {
        return forumId
    }

    fun setForumId(forumId: String)
    {
        this.forumId = forumId
    }
}