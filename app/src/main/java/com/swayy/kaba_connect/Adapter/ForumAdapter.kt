package com.swayy.kaba_connect.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.ForumsActivity
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Forum
import com.swayy.kaba_connect.model.Post
import com.swayy.kaba_connect.model.User

class ForumAdapter(private var mContext: Context,
                   private  var mForum: ArrayList<Forum>): RecyclerView.Adapter<ForumAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumAdapter.ViewHolder {
        val view  = LayoutInflater.from(mContext).inflate(R.layout.forum_item_layout, parent, false)
        return ForumAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForumAdapter.ViewHolder, position: Int) {
        val user = mForum[position]
        holder.fullnametextview.text = user.getName()
        holder.mobiletextview.text = user.getDescription()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.placeholderfour).into(holder.im)
        holder.btnus.setOnClickListener {
            val intentComment = Intent(mContext, ForumsActivity::class.java)
            intentComment.putExtra("postId", user.getForumId())
            intentComment.putExtra("publisherId", user.getPublisher())
            mContext.startActivity(intentComment)
        }
    }

    override fun getItemCount(): Int {
        return mForum.size
    }

    fun clear() {
        //feed is your model class
        mForum.clear()
        notifyDataSetChanged()
    }

    fun addAll(forums: MutableList<Forum>?) {
        forums!!.addAll(forums)
    }


    class ViewHolder(@NonNull itemView : View): RecyclerView.ViewHolder(itemView)
    {
        var fullnametextview: TextView = itemView.findViewById(R.id.us_name_forum)
        var mobiletextview: TextView = itemView.findViewById(R.id.us_name_course_forum)
        var im: ImageView = itemView.findViewById(R.id.pro_image_forum)
        var btnus: Button = itemView.findViewById(R.id.btnSignInusforum)


    }
}