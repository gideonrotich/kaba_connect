package com.swayy.kaba_connect.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.swayy.kaba_connect.Adapter.KabaAdapter
import com.swayy.kaba_connect.Adapter.PostAdapter
import com.swayy.kaba_connect.CustomBottomSheetDialogFragment
import com.swayy.kaba_connect.Forum.CreateforumActivity
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Forum
import com.swayy.kaba_connect.model.Post
import com.swayy.kaba_connect.model.User
import com.swayy.kaba_connect.model.Verified
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_forums.*
import kotlinx.android.synthetic.main.fragment_forums.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class forumsFragment : Fragment() {
    private var kabaAdapter: KabaAdapter? = null
    private var forumList:MutableList<Forum>? = null
    private lateinit var firebaseUser: FirebaseUser
    private var mSwipe: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_forums, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        view.profoda.setOnClickListener {

            firebaseUser = FirebaseAuth.getInstance().currentUser!!
            val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",firebaseUser.uid.toString())
            pref?.apply()


            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            activity?.let { it1 -> modalbottomSheetFragment.show(it1.supportFragmentManager,modalbottomSheetFragment.tag) }


        }

        view.btncreate.setOnClickListener { startActivity(Intent(context,CreateforumActivity::class.java)) }


        var recyclerView: RecyclerView? = null
//
        recyclerView = view.findViewById(R.id.recycler_view_forum)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        forumList = ArrayList()
        kabaAdapter = context?.let { KabaAdapter(it, forumList as ArrayList<Forum>) }
        recyclerView.adapter = kabaAdapter


        userinfo()

        view.swipe_primary.post(Runnable {
            view.swipe_primary.isRefreshing=true
            retrieveForums()
        })

        view.swipe_primary.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            kabaAdapter!!.clear()
            kabaAdapter!!.addAll(forumList)
            //loadin
            retrieveForums()
        })


        return view
    }

    private fun userinfo() {
        val jobsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)

        jobsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Glide.with(context!!)  //2
                        .load(user!!.getImage()) //3
                        .centerCrop() //4
                        .into(profoda)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun retrieveForums() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("Forum")

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                forumList?.clear()

                for (snapshot in p0.children)
                {
                    val post = snapshot.getValue(Forum::class.java)

                    if (post != null)
                    {
                        forumList!!.add(post)
                        view!!.swipe_primary.isRefreshing=false
                    }
                    kabaAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}