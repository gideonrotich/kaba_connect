package com.swayy.kaba_connect.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.swayy.kaba_connect.Fragments.discoverFragment
import com.swayy.kaba_connect.Fragments.profileFragment
import com.swayy.kaba_connect.R
import kotlinx.android.synthetic.main.activity_feedback.*

class feedbackActivity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        btnSignInfacebookfood.setOnClickListener {
            sendFeedback()
        }

        opt.setOnClickListener {
            (this as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, profileFragment()).commit()
        }

    }

    private fun sendFeedback() {
        when{
            etEmailfeed.text.toString() == "" -> {
                Toast.makeText(this,"Write feedback first", Toast.LENGTH_LONG).show()
            }
            etEmailfood.text.toString() == "" -> {
                Toast.makeText(this,"Write email first", Toast.LENGTH_LONG).show()
            }
            else ->{
                val jobsRef = FirebaseDatabase.getInstance().reference.child("Feedback").child(firebaseUser.uid)

                val userMap = HashMap<String, Any>()
                userMap["feedback"] = etEmailfeed.text.toString().toLowerCase()
                userMap["email"] = etEmailfood.text.toString().toLowerCase()


                jobsRef.setValue(userMap)

                Toast.makeText(this,"Feedback sent successfully", Toast.LENGTH_LONG).show()
            }
        }
    }

}