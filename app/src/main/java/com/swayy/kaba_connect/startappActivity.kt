package com.swayy.kaba_connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.swayy.kaba_connect.Registration.Splash_screen_Activity

class startappActivity : AppCompatActivity() {
    private val SPLASH_TIME: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startapp)

        if (FirebaseAuth.getInstance().currentUser == null)
        {

            startActivity(Intent(this, Splash_screen_Activity::class.java))

        }
        else {
            Handler().postDelayed({

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, SPLASH_TIME)
        }
    }
}