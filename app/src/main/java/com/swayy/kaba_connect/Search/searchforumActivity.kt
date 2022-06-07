package com.swayy.kaba_connect.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.swayy.kaba_connect.Adapter.ForumAdapter
import com.swayy.kaba_connect.Fragments.discoverFragment
import com.swayy.kaba_connect.Fragments.notificationFragment
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Forum
import kotlinx.android.synthetic.main.activity_searchforum.*

class searchforumActivity : AppCompatActivity() {
    private var forumAdapter: ForumAdapter? = null
    private var mForum:MutableList<Forum>? = null
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchforum)

        optome.setOnClickListener {
            (this as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, discoverFragment()).commit()
        }


        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var recyclerviewmodcom: RecyclerView? = null

        val num = 2

        recyclerviewmodcom = findViewById(R.id.recycler_view_search)
        recyclerviewmodcom?.setHasFixedSize(true)
        recyclerviewmodcom?.itemAnimator = DefaultItemAnimator()
        val linearLayout = GridLayoutManager(this,num)
        linearLayout.reverseLayout = true
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        recyclerviewmodcom?.layoutManager = linearLayout
        mForum = ArrayList()
        forumAdapter = this?.let { ForumAdapter(it,mForum as ArrayList<Forum>) }
        recyclerviewmodcom?.adapter = forumAdapter
        recyclerviewmodcom?.visibility = View.VISIBLE


        hua.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (hua.text.toString() == "")
                {

                }
                else
                {


                    val query = FirebaseDatabase.getInstance().getReference("Forum")
                            .orderByChild("name")
                            .startAt(s.toString().toLowerCase()).endAt(s.toString().toLowerCase() + "\uf8ff")


                        query.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot)
                            {
                                mForum?.clear()
                                if (p0.exists())
                                {
                                    for (snapshot in p0.children)
                                    {
                                        val job = snapshot.getValue(Forum::class.java)
                                        if (job != null)
                                        {
                                            mForum?.add(job)
//                                            view!!.mmm.visibility = View.GONE
                                        }

                                    }
                                    forumAdapter?.notifyDataSetChanged()
                                }
                                else
                                {
                                    Toast.makeText(this@searchforumActivity,"Forum not found", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                        val jobsRefm = FirebaseDatabase.getInstance().getReference("Forum")
                        jobsRefm.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                if (hua?.text.toString() == "")
                                {
                                    mForum?.clear()

                                    for (snapshot in p0.children)
                                    {
                                        val job = snapshot.getValue(Forum::class.java)
                                        if (job != null)
                                        {
                                            mForum?.add(job)
                                        }
                                    }

                                    forumAdapter?.notifyDataSetChanged()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                        ///////////////////////////////////////////////////////////

                        val searchtext = hua.text.toString()

                        when {
                            TextUtils.isEmpty(searchtext) -> Toast.makeText(
                                this@searchforumActivity,
                                "input search item first",
                                Toast.LENGTH_LONG
                            ).show()

                            else ->{
                                val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
                                val time = System.currentTimeMillis().toString()
                                val verifiedRef: DatabaseReference =
                                    FirebaseDatabase.getInstance().reference.child("Searchtext").child(firebaseUser.uid)
                                val key = verifiedRef.push().key

                                val userMap = HashMap<String, Any>()
                                userMap["searchtext"] = searchtext.toLowerCase()
                                userMap["uid"] = currentUserID


                                if (key != null) {
                                    verifiedRef.child(key).setValue(userMap)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {


                                            } else {
                                                val message = task.exception!!.toString()
                                                Toast.makeText(this@searchforumActivity, "Error: $message", Toast.LENGTH_LONG).show()
                                                FirebaseAuth.getInstance().signOut()

                                            }
                                        }
                                }
                            }
                        }


                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })



    }
}