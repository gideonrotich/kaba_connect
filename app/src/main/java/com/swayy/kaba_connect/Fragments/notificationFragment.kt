package com.swayy.kaba_connect.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.swayy.kaba_connect.Adapter.NotificationAdapter
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Notification
import com.swayy.kaba_connect.model.User
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_notification.view.*


class notificationFragment : Fragment() {
    private var notificationAdapter: NotificationAdapter? = null
    private var mNotification:MutableList<Notification>? = null
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_notification, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!


        var recyclerView: RecyclerView

        recyclerView = view.findViewById(R.id.recycler_view_notifications)
        val linearLayoutManager =  LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        recyclerView.layoutManager = linearLayoutManager

        mNotification = ArrayList()
        notificationAdapter = context?.let { NotificationAdapter(it, mNotification) }
        recyclerView.adapter = notificationAdapter


            val jobsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)

            jobsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists())
                    {
                        val user = p0.getValue<User>(User::class.java)

                        Glide.with(context!!)  //2
                            .load(user!!.getImage()) //3
                            .centerCrop() //4
                            .into(view!!.optv)


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



        val commentsRef = FirebaseDatabase.getInstance().reference.child("Notifications").child(firebaseUser.uid)

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    mNotification!!.clear()

                    for (snapshot in p0.children)
                    {
                        val comment = snapshot.getValue(Notification::class.java)
                        mNotification!!.add(comment!!)
                    }

                    notificationAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })




        return  view
    }


}