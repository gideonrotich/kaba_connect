package com.swayy.kaba_connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.Adapter.CommentsAdapter
import com.swayy.kaba_connect.Adapter.FaraAdapter
import com.swayy.kaba_connect.model.Comment
import com.swayy.kaba_connect.model.Fara
import com.swayy.kaba_connect.model.User
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_comments.add_comment
import kotlinx.android.synthetic.main.activity_forums.*

class ForumsActivity : AppCompatActivity() {
    private var postId  = ""
    private var publisherId  = ""
    private var firebaseUser: FirebaseUser? = null
    private var faraAdapter: FaraAdapter? = null
    private var faraList: MutableList<com.swayy.kaba_connect.model.Fara>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forums)

        val intent = intent
        postId = intent.getStringExtra("postId").toString()
        publisherId = intent.getStringExtra("publisherId").toString()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recycler_view_fara)
        val linearLayoutManager =  LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = false
        recyclerView.layoutManager = linearLayoutManager


        faraList = ArrayList()
        faraAdapter = FaraAdapter(this, faraList)
        recyclerView.adapter = faraAdapter
        recyclerView.scrollToPosition(faraAdapter!!.itemCount-1)

        readComments()
        getDescription()
        getDescriptions()
        getPostImage()



        post_fara.setOnClickListener(View.OnClickListener {
            if (add_fara!!.text.toString() == "")
            {
                Toast.makeText(this,"Please write post first", Toast.LENGTH_LONG).show()
            }
            else
            {
                addPost()
            }
        })

    }

    private fun addPost() {
        val faraRef = FirebaseDatabase.getInstance().reference.child("Fara").child(postId)

        val commentsMap = HashMap<String, Any>()
        commentsMap["post"] = add_fara!!.text.toString()
        commentsMap["publisher"] = firebaseUser!!.uid

        faraRef.push().setValue(commentsMap)

        add_fara!!.text.clear()
    }

    private fun getPostImage()
    {
        val postRef = FirebaseDatabase.getInstance().reference.child("Forum").child(postId!!).child("image")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val image = p0.value.toString()

                    Picasso.get().load(image).placeholder(R.drawable.placeholderthree).into(proffg)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun readComments(){
        val commentsRef = FirebaseDatabase.getInstance().reference.child("Fara").child(postId)

        commentsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    faraList!!.clear()

                    for (snapshot in p0.children)
                    {
                        val comment = snapshot.getValue(Fara::class.java)
                        faraList!!.add(comment!!)
                    }

                    faraAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getDescription()
    {
        val postRef = FirebaseDatabase.getInstance().reference.child("Forum").child(postId!!).child("name")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val description = p0.value.toString()

                    comp.text = description


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun getDescriptions()
    {
        val postRef = FirebaseDatabase.getInstance().reference.child("Forum").child(postId!!).child("description")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val description = p0.value.toString()

                    comps.text = description


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}