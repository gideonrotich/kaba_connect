package com.swayy.kaba_connect.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
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
import com.swayy.kaba_connect.Adapter.ForumAdapter
import com.swayy.kaba_connect.Adapter.ItemAdapter
import com.swayy.kaba_connect.Adapter.PostAdapter
import com.swayy.kaba_connect.CustomBottomSheetDialogFragment
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Forum
import com.swayy.kaba_connect.model.Post
import com.swayy.kaba_connect.model.User
import com.swayy.kaba_connect.model.Verified
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_discover.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class discoverFragment : Fragment() {

    private var itemAdapter: ItemAdapter? = null
    private var itemList:MutableList<User>? = null
    private var postAdapter: PostAdapter? = null
    private var postList:MutableList<Post>? = null
    private var forumAdapter: ForumAdapter?  = null
    private var forumList:MutableList<Forum>? = null
    private var verifiedlist:MutableList<Verified>? = null
    private lateinit var firebaseUser: FirebaseUser
    private var mSwipe: SwipeRefreshLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_discover, container, false)


        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        view.profod.setOnClickListener {

            firebaseUser = FirebaseAuth.getInstance().currentUser!!
            val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",firebaseUser.uid.toString())
            pref?.apply()


            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            activity?.let { it1 -> modalbottomSheetFragment.show(it1.supportFragmentManager,modalbottomSheetFragment.tag) }


        }



        var recyclerViewdis: RecyclerView? = null
//
        recyclerViewdis = view.findViewById(R.id.recycler_view_yetu)
        val linearLayoutManagertwo = LinearLayoutManager(context)
        linearLayoutManagertwo.reverseLayout = false
        recyclerViewdis.layoutManager = linearLayoutManagertwo

        postList = ArrayList()
        verifiedlist = ArrayList()
        postAdapter = context?.let { PostAdapter(it, postList as ArrayList<Post>,verifiedlist as ArrayList<Verified>) }
        recyclerViewdis.adapter = postAdapter

        var recyclerViewforum: RecyclerView? = null
//
        recyclerViewforum = view.findViewById(R.id.recycler_view_forad)
        val linearLayoutManagert = GridLayoutManager(context,2)
        linearLayoutManagert.reverseLayout = true
        recyclerViewforum.layoutManager = linearLayoutManagert

        forumList = ArrayList()
        forumAdapter = context?.let { ForumAdapter(it, forumList as ArrayList<Forum>) }
        recyclerViewforum.adapter = forumAdapter


        userinfo()
        view.swipe_secondary.post(Runnable {
            view.swipe_secondary.isRefreshing=true
            retrievePosts()
            retrieveForum()
        })

        view.swipe_secondary.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            forumAdapter!!.clear()
            postAdapter!!.clear()
            forumAdapter!!.addAll(forumList)
            postAdapter!!.addAll(postList)
            //loadin
            retrievePosts()
            retrieveForum()
        })


        return view
    }

    private fun retrieveForum() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("Forum").limitToFirst(4)

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                forumList?.clear()

                for (snapshot in p0.children)
                {
                    val post = snapshot.getValue(Forum::class.java)

                    if (post != null)
                    {
                        forumList!!.add(post)
                        view!!.swipe_secondary.isRefreshing=false
                    }
                    forumAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
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
                        .into(profod)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("Posts").limitToLast(2)

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()

                for (snapshot in p0.children)
                {
                    val post = snapshot.getValue(Post::class.java)

                    if (post != null)
                    {
                        postList!!.add(post)
                        view!!.swipe_secondary.isRefreshing=false
                    }
                    postAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}