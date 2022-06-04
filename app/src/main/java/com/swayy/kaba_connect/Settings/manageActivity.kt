package com.swayy.kaba_connect.Settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.swayy.kaba_connect.MainActivity
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.Registration.SigninActivity
import kotlinx.android.synthetic.main.activity_manage.*

class manageActivity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        btntoad.setOnClickListener {
            val eBuilder = AlertDialog.Builder(this)
            eBuilder.setCancelable(false)

            eBuilder.setMessage("Are you sure you want to delete your account?")
            eBuilder.setPositiveButton("Yes")


            {

                    Dialog,which->
                val ref = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
                ref.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {

                        firebaseUser.delete()
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(this,"Your account has been deleted successfully..", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, SigninActivity::class.java))


                    }

                    if (task.isCanceled)
                    {
                        Toast.makeText(this,"Something went wrong...Try again later", Toast.LENGTH_LONG).show()
                    }
                }
            }
            eBuilder.setNegativeButton("No")
            {
                    Dialog,which ->


            }
            val createBuild  = eBuilder.create()
            createBuild.show()

        }

    }
}