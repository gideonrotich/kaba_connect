package com.swayy.kaba_connect.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
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
import com.swayy.kaba_connect.*
import com.swayy.kaba_connect.Adapter.ItemAdapter
import com.swayy.kaba_connect.Adapter.PostAdapter
import com.swayy.kaba_connect.model.Post
import com.swayy.kaba_connect.model.User
import com.swayy.kaba_connect.model.Verified
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_discover.view.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class FeedFragment : Fragment() {
    private var itemAdapter: ItemAdapter? = null
    private var itemList:MutableList<User>? = null
    private lateinit var firebaseUser: FirebaseUser
    private var verifiedlist:MutableList<Verified>? = null
    private var postAdapter: PostAdapter? = null
    private var postList:MutableList<Post>? = null
    private var mSwipe: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_feed, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        view.whatif.setOnClickListener {
            startActivity(Intent(context,LatestMessagesActivity::class.java))
        }
        view.what.setOnClickListener {
            startActivity(Intent(context,AddPostActivity::class.java))
        }
//        view.user_name_course.setOnClickListener {
//            startActivity(Intent(context,LatestMessagesActivity::class.java))
//        }
        view.circ_one.setOnClickListener {
            val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",firebaseUser.uid)
            pref?.apply()
            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            modalbottomSheetFragment.show((context as FragmentActivity).supportFragmentManager,modalbottomSheetFragment.tag)
        }

        var recyclerView: RecyclerView? = null
//
        recyclerView = view.findViewById(R.id.recycler_view_feed)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = false
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = linearLayoutManager

        itemList = ArrayList()
        itemAdapter = context?.let { ItemAdapter(it, itemList as ArrayList<User>,firebaseUser) }
        recyclerView.adapter = itemAdapter

        var recyclerViewa: RecyclerView? = null
        recyclerViewa = view.findViewById(R.id.recycler_view_posta)
        val linearLayoutManagera = LinearLayoutManager(context)
        linearLayoutManagera.reverseLayout = true
        linearLayoutManagera.stackFromEnd = true
        recyclerViewa.layoutManager = linearLayoutManagera

        postList = ArrayList()
        verifiedlist = ArrayList()
        postAdapter = context?.let { PostAdapter(it, postList as ArrayList<Post>,verifiedlist as ArrayList<Verified>) }
        recyclerViewa.adapter = postAdapter


        //get notifications count
        val followerssRef = FirebaseDatabase.getInstance().reference.child("Notifications").child(firebaseUser.uid)


//        followerssRef.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists())
//                {
//                    view?.card_badge?.visibility = View.VISIBLE
//                    view?.neyo?.text = snapshot.childrenCount.toString()
//
//
//                }
//                else{
////                        view?.ajedma?.text = "0"
////                    view.opttomo.setImageResource(R.drawable.ic_notifications)
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })


        userinfo()


        view.swipe_mimi.post(Runnable {
            view.swipe_mimi.isRefreshing=true
            retrievePosts()
            retrieveUsers()
        })

        view.swipe_mimi.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            postAdapter!!.clear()
            itemAdapter!!.clear()
            itemAdapter!!.addAll(itemList)
            postAdapter!!.addAll(postList)
            //loadin
            retrievePosts()
            retrieveUsers()
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

                    user_name_feed.text = user!!.getFullname()
                    user_name_course.text = user!!.getMobile()
                    Glide.with(context!!)  //2
                        .load(user!!.getImage()) //3
                        .centerCrop() //4
                        .into(circ_one)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun retrieveUsers() {
        val postsRef = FirebaseDatabase.getInstance().reference.child("Users")

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                itemList?.clear()

                for (snapshot in p0.children)
                {
                    val post = snapshot.getValue(User::class.java)

                    if (post != null)
                    {
                        itemList!!.add(post)
                        view!!.swipe_mimi.isRefreshing=false

                    }
                    itemAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

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
                        view!!.swipe_mimi.isRefreshing=false

                    }
                    postAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}