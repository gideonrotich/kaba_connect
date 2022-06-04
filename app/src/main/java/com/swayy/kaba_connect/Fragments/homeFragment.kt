package com.swayy.kaba_connect.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.swayy.kaba_connect.Adapter.PostAdapter
import com.swayy.kaba_connect.AddPostActivity
import com.swayy.kaba_connect.CustomBottomSheetDialogFragment
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Post
import com.swayy.kaba_connect.model.User
import com.swayy.kaba_connect.model.Verified
import com.swayy.kaba_connect.noteActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class homeFragment : Fragment() {

    private var postAdapter: PostAdapter? = null
    private var postList:MutableList<Post>? = null
    private var verifiedlist:MutableList<Verified>? = null
    private lateinit var firebaseUser: FirebaseUser
    private var mSwipe: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        view.fab.setOnClickListener {
            startActivity(Intent(context, AddPostActivity::class.java))
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!


        var recyclerView: RecyclerView? = null
//
        recyclerView = view.findViewById(R.id.recycler_view_post)
        mSwipe = view.findViewById(R.id.swipe_main)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        verifiedlist = ArrayList()
        postAdapter = context?.let { PostAdapter(it, postList as ArrayList<Post>,verifiedlist as ArrayList<Verified>) }
        recyclerView.adapter = postAdapter

        //for profile dialog pop up
        view.profo.setOnClickListener {

                firebaseUser = FirebaseAuth.getInstance().currentUser!!
                val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
                pref?.putString("userid",firebaseUser.uid.toString())
                pref?.apply()


                val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
                activity?.let { it1 -> modalbottomSheetFragment.show(it1.supportFragmentManager,modalbottomSheetFragment.tag) }


        }

        //call the shimmer layout

       // retrievePosts()
        userinfo()
        //now we set onSwipe here
        //try run
        view.not.setOnClickListener {
            startActivity(Intent(context, noteActivity::class.java))
        }


        view.swipe_main.post(Runnable {
            view.swipe_main.isRefreshing=true
            retrievePosts()
        })

        view.swipe_main.setOnRefreshListener(OnRefreshListener {
            postAdapter!!.clear()
            postAdapter!!.addAll(postList)
            //loadin
            retrievePosts()
        })

        return view
    }



    //retrieve user info from firebase
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
                        .into(profo)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //retrieve posts from firebase
    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()

                for (snapshot in p0.children)
                {
                    val post = snapshot.getValue(Post::class.java)

                    if (post != null)
                    {
                        postList!!.add(post)
                        view!!.swipe_main.isRefreshing=false
                    }
                    postAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}