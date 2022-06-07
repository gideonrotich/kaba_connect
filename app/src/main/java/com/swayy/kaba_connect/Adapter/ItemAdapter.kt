package com.swayy.kaba_connect.Adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.CustomBottomSheetDialogFragment
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Forum
import com.swayy.kaba_connect.model.User

class ItemAdapter(private var mContext: Context,
                  private  var mUser: ArrayList<User>,
                  private  var firebaseUser: FirebaseUser
): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val view  = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return ItemAdapter.ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mUser.size
    }

    fun clear() {
        //feed is your model class
        mUser.clear()
        notifyDataSetChanged()
    }

    fun addAll(user: MutableList<User>?) {
        user!!.addAll(user)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val user = mUser[position]
        holder.fullnametextview.text = user.getFullname()
        holder.mobiletextview.text = user.getMobile()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.placeholderthree).into(holder.im)
            Picasso.get().load(user.getImage()).into(holder.im_main)




        if(user.getFullname() == "gideon rotich"){
            holder.dat.visibility = View.VISIBLE
        }
        if(user.getFullname() == "tonny bett")
        {
            holder.dat.visibility = View.VISIBLE
        }







        holder.im.setOnClickListener {
            val pref = mContext?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",user.getUid())
            pref?.apply()
            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            modalbottomSheetFragment.show((mContext as FragmentActivity).supportFragmentManager,modalbottomSheetFragment.tag)
        }
        holder.fullnametextview.setOnClickListener {
            val pref = mContext?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",user.getUid())
            pref?.apply()
            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            modalbottomSheetFragment.show((mContext as FragmentActivity).supportFragmentManager,modalbottomSheetFragment.tag)
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val followingRef = firebaseUser.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference.child("Follow").child(it1.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(user.getUid()).exists())
                {
                    holder.followbtn.text = "Following"
                }
                else{
                    holder.followbtn.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        //
        holder.followbtn.setOnClickListener {
            if (holder.followbtn.text.toString() == "Follow")
            {
                firebaseUser.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("Follow").child(it1.toString()).child("Following").child(user.getUid())
                        .setValue(true).addOnCompleteListener { task ->
                            if (task.isSuccessful)
                            {

                                firebaseUser.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference.child("Follow").child(user.getUid()).child("Followers").child(it1.toString())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if (task.isSuccessful)
                                            {
                                                val notRef = FirebaseDatabase.getInstance().reference.child("Notifications").child(user.getUid())

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
                    FirebaseDatabase.getInstance().reference.child("Follow").child(it1.toString()).child("Following").child(user.getUid())
                        .removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful)
                            {

                                firebaseUser.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference.child("Follow").child(user.getUid()).child("Followers").child(it1.toString())
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
        //
    }


    class ViewHolder(@NonNull itemView : View): RecyclerView.ViewHolder(itemView)
    {
        var fullnametextview: TextView = itemView.findViewById(R.id.us_name)
        var mobiletextview: TextView = itemView.findViewById(R.id.us_name_course)
        var im: ImageView = itemView.findViewById(R.id.pro_image)
        var followbtn: Button = itemView.findViewById(R.id.btnSignInus)
        var im_main:ImageView = itemView.findViewById(R.id.image_main)
        var dat:ImageView = itemView.findViewById(R.id.verificationdata)



    }

}