package com.swayy.kaba_connect.Registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.swayy.kaba_connect.MainActivity
import com.swayy.kaba_connect.R
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_splash_screen.*

class Splash_screen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        log.setOnClickListener { startActivity(Intent(this, SigninActivity::class.java))
        }
        loggooglea.setOnClickListener {
            Toast.makeText(this,"Use Email to sign in", Toast.LENGTH_LONG).show()
        }
        logfacebooka.setOnClickListener {
            Toast.makeText(this,"Use Email to sign in", Toast.LENGTH_LONG).show()
        }


    }
    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null)
        {
            val intent = Intent(this@Splash_screen_Activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}