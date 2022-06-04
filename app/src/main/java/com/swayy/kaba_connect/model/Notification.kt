package com.swayy.kaba_connect.model

class Notification {
    private var notification: String = ""
    private var publisher: String = ""
    private var image: String = ""

    constructor()

    constructor(notification: String, publisher: String,image: String) {
        this.notification = notification
        this.publisher = publisher
        this.image = image
    }

    fun getNotification(): String{
        return notification
    }

    fun getPublisher(): String{
        return publisher
    }
    fun getImage(): String{
        return image
    }

    fun setNotification(notification: String){
        this.notification = notification
    }

    fun setPublisher(publisher: String){
        this.publisher = publisher
    }

    fun setImage(image: String){
        this.image = image
    }
}