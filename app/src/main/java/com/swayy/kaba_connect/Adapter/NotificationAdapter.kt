package com.swayy.kaba_connect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.CustomBottomSheetDialogFragment
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Notification
import com.swayy.kaba_connect.model.User
import de.hdodenhof.circleimageview.CircleImageView

class NotificationAdapter(private val mContext: Context,
                          private val mNotification: MutableList<Notification>?) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>()
{
    private var firebaseUser: FirebaseUser? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.notification_item_layout, parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val comment = mNotification!![position]
        holder.commentTV.text = comment.getNotification()
        if (comment.getNotification() == "Your forum is live")
        {
            Picasso.get().load(comment.getImage()).into(holder.maji)
            holder.userNameTV.visibility = View.GONE
            
        }
        if (comment.getNotification() == "welcome to kabarak connect")
        {
            holder.userNameTV.visibility = View.GONE
        }
        getUserInfo(holder.imageProfile, holder.userNameTV, comment.getPublisher())

        holder.imageProfile.setOnClickListener {
            val pref = mContext?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",comment.getPublisher())
            pref?.apply()
            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            modalbottomSheetFragment.show((mContext as FragmentActivity).supportFragmentManager,modalbottomSheetFragment.tag)
        }
        holder.userNameTV.setOnClickListener {
            val pref = mContext?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",comment.getPublisher())
            pref?.apply()
            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            modalbottomSheetFragment.show((mContext as FragmentActivity).supportFragmentManager,modalbottomSheetFragment.tag)
        }
    }



    private fun getUserInfo(imageProfile: CircleImageView, userNameTV: TextView, publisher: String) {
        val userREf = FirebaseDatabase.getInstance().reference.child("Users").child(publisher)

        userREf.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    val user = p0.getValue(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.placeholderthree).into(imageProfile)

                    userNameTV.text = user.getFullname()


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun getItemCount(): Int {
       return mNotification!!.size
    }


    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var imageProfile: CircleImageView
        var userNameTV: TextView
        var commentTV: TextView
        var maji: ImageView

        init {
            imageProfile = itemView.findViewById(R.id.user_profile_image_commentn)
            userNameTV = itemView.findViewById(R.id.user_name_commentn)
            commentTV = itemView.findViewById(R.id.comment_commentn)
            maji = itemView.findViewById(R.id.marefu)
        }
    }

}