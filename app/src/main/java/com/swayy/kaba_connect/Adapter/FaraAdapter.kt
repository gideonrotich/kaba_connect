package com.swayy.kaba_connect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.User
import de.hdodenhof.circleimageview.CircleImageView

class FaraAdapter(private val mContext: Context,
                  private val mFara: MutableList<com.swayy.kaba_connect.model.Fara>?) :
    RecyclerView.Adapter<FaraAdapter.ViewHolder>()
{
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaraAdapter.ViewHolder
    {
        val view = LayoutInflater.from(mContext).inflate(R.layout.post_forum_item_layout, parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaraAdapter.ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val comment = mFara!![position]
        holder.commentTVforum.text = comment.getPost()

        getUserInfo(holder.imageProfile,holder.userNameTVforum, comment.getPublisher())
    }

    override fun getItemCount(): Int {
        return mFara!!.size
    }

    private fun getUserInfo(imageProfile: CircleImageView,userNameTV: TextView, publisher: String) {
        val userREf = FirebaseDatabase.getInstance().reference.child("Users").child(publisher)

        userREf.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    val user = p0.getValue(User::class.java)

                    userNameTV.text = user!!.getFullname()
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.placeholderthree).into(imageProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var userNameTVforum: TextView
        var commentTVforum: TextView
        var imageProfile: CircleImageView

        init {
            userNameTVforum = itemView.findViewById(R.id.user_name_forum)
            commentTVforum = itemView.findViewById(R.id.mew)
            imageProfile = itemView.findViewById(R.id.user_profile_image_fara)
        }
    }
}