package com.swayy.kaba_connect.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.swayy.kaba_connect.Profile.ProfileActivity
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.Registration.SigninActivity
import com.swayy.kaba_connect.Settings.feedbackActivity
import com.swayy.kaba_connect.Settings.manageActivity
import com.swayy.kaba_connect.Settings.settingsActivity
import com.swayy.kaba_connect.model.User
import kotlinx.android.synthetic.main.fragment_profile.view.*


class profileFragment : Fragment() {
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        view.ndakugundar.setOnClickListener {
            startActivity(Intent(context,ProfileActivity::class.java))
        }
        view.march.setOnClickListener {
            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout,notificationFragment()).commit()
        }
        view.april.setOnClickListener {
            startActivity(Intent(context,feedbackActivity::class.java))
        }
        view.may.setOnClickListener {
            startActivity(Intent(context,manageActivity::class.java))
        }
        view.ndakugunda.setOnClickListener {
            startActivity(Intent(context,settingsActivity::class.java))
        }
        view.june.setOnClickListener {
            val url = "http://www.kabarak.ac.ke/contact-us"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        view.ndakugundasong.setOnClickListener {
            val url = "http://www.kabarak.ac.ke/wp-content/uploads/2018/01/Kabarak_prospectus_2020.pdf"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


        view.btnSignudp.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(context,SigninActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        return view
    }


}