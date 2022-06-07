package com.swayy.kaba_connect

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.Adapter.PostAdapter
import com.swayy.kaba_connect.Adapter.UserAdapter
import com.swayy.kaba_connect.Profile.ProfileActivity
import com.swayy.kaba_connect.model.Post
import com.swayy.kaba_connect.model.User
import com.swayy.kaba_connect.model.Userr
import com.swayy.kaba_connect.model.Verified
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.faidooa
import kotlinx.android.synthetic.main.layout_bottom_sheet.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
/**
 * Code written by Gideon Rotich
 */
class CustomBottomSheetDialogFragment: BottomSheetDialogFragment() {
    private var mUser:MutableList<User>? = null
    private var userAdapter: UserAdapter? = null
    private var postAdapter: PostAdapter? = null
    private var postList:MutableList<Post>? = null
    private var verifiedlist:MutableList<Verified>? = null
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var userid:String

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
        val USER_KEY = "USER_KEY"
        val USER_KEY_IMAGE = "USER_KEY_IMAGE"
        val USER_KEY_PROFESSION = "USER_KEY_PROFESSION"
        val USER_KEY_UID = "USER_KEY_UID"

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.layout_bottom_sheet,container,false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null)
        {
            this.userid = pref.getString("userid","none").toString()

        }



        val jobsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid)

        jobsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Glide.with(context!!)  //2
                        .load(user!!.getImage()).placeholder(R.drawable.twoo) //3
                        .centerCrop() //4
                        .into(view!!.dpyao)
                    view.mamayaoaa.text = user!!.getFullname()
                    view.webmeka.text = user!!.getMobile()
                    view.webmeksa.text = user!!.getTime()

                    val testing = user!!.getUid()

                    val followingRef = firebaseUser.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("Follow").child(it1.toString())
                            .child("Following")
                    }



                    followingRef.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.child(testing).exists())
                            {
                                view?.faizaay?.text = "Following"
                            }
                            else{
                                view?.faizaay?.text = "Follow"
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                    val followersRef = FirebaseDatabase.getInstance().reference.child("Follow").child(testing)
                        .child("Following")


                    followersRef.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists())
                            {
                                view?.ajedmma?.text = snapshot.childrenCount.toString()
                            }
                            else{
                                view?.ajedmma?.text = "0"
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })


                    val followerssRef = FirebaseDatabase.getInstance().reference.child("Follow").child(testing)
                        .child("Followers")


                    followerssRef.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists())
                            {
                                view?.ajedma?.text = snapshot.childrenCount.toString()
                            }
                            else{
                                view?.ajedma?.text = "0"
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })


                    view?.faizaay?.setOnClickListener {
                        if (view.faizaay?.text.toString() == "Follow")
                        {
                            firebaseUser.uid.let { it1 ->
                                FirebaseDatabase.getInstance().reference.child("Follow").child(it1.toString()).child("Following").child(testing)
                                    .setValue(true).addOnCompleteListener { task ->
                                        if (task.isSuccessful)
                                        {

                                            firebaseUser.uid.let { it1 ->
                                                FirebaseDatabase.getInstance().reference.child("Follow").child(testing).child("Followers").child(it1.toString())
                                                    .setValue(true).addOnCompleteListener { task ->
                                                        if (task.isSuccessful)
                                                        {
                                                            val notRef = FirebaseDatabase.getInstance().reference.child("Notifications").child(testing)

                                                            val commentsMap = HashMap<String, Any>()
                                                            commentsMap["notification"] = "started following you"
                                                            commentsMap["publisher"] = firebaseUser!!.uid
                                                            commentsMap["image"] = ""

                                                            notRef.push().setValue(commentsMap)
                                                        }
                                                    }
                                            }
                                        }
                                    }
                            }
                        }
                        else
                        {
                            firebaseUser.uid.let { it1 ->
                                FirebaseDatabase.getInstance().reference.child("Follow").child(it1.toString()).child("Following").child(testing)
                                    .removeValue().addOnCompleteListener { task ->
                                        if (task.isSuccessful)
                                        {

                                            firebaseUser.uid.let { it1 ->
                                                FirebaseDatabase.getInstance().reference.child("Follow").child(testing).child("Followers").child(it1.toString())
                                                    .removeValue().addOnCompleteListener { task ->
                                                        if (task.isSuccessful)
                                                        {

                                                        }
                                                    }
                                            }




                                        }
                                    }
                            }
                        }
                    }

                    if (user.getUid() == firebaseUser.uid)
                    {
                        view.faidooa.text = "Edit profile"

                        view.faidooa.setOnClickListener(View.OnClickListener {
                            val pref = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                            pref.putString("userid",user.getUid())
                            pref.apply()
                            startActivity(Intent(context, ProfileActivity::class.java))
                        })
                    }

                    if (user.getUid() != firebaseUser.uid)
                    {
                        view.faidooa.text = "Message"

                        val wewe = user!!.getUid()

                        val jobsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(wewe)

                        jobsRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {

                                if (p0.exists())
                                {
                                    val user = p0.getValue<User>(User::class.java)
                                    faidooa.setOnClickListener(View.OnClickListener { view ->
                                        var userItem = view as testActivity.UserItem
                                    })

                                    faidooa.setOnClickListener {
//                                        val packageManager = context!!.packageManager
//                                        val toNumber:String = "254"+user!!.getMobile()
//                                        val indi = user!!.getFullname()
//                                        val text:String = "Hello, am "+indi +" and i would like to talk to you"
//                                        val sendIntent = Intent(Intent.ACTION_VIEW)
//                                        sendIntent.data = Uri.parse("http://api.whatsapp.com/send?phone="+toNumber + "&text="+text)
//                                        startActivity(sendIntent)




                                    }

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    //test


    //
}