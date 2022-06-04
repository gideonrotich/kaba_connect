package com.swayy.kaba_connect.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.CommentsActivity
import com.swayy.kaba_connect.ForumsActivity
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Forum
import com.swayy.kaba_connect.model.User

class KabaAdapter(private var mContext: Context,
                  private  var mKaba: ArrayList<Forum>): RecyclerView.Adapter<KabaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KabaAdapter.ViewHolder {
        val view  = LayoutInflater.from(mContext).inflate(R.layout.forum_layout, parent, false)
        return KabaAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: KabaAdapter.ViewHolder, position: Int) {
        val user = mKaba[position]
        holder.fullnametextview.text = user.getName()
        holder.mobiletextview.text = user.getDescription()

        holder.itemView.setOnClickListener {
            val intentComment = Intent(mContext, ForumsActivity::class.java)
            intentComment.putExtra("postId", user.getForumId())
            intentComment.putExtra("publisherId", user.getPublisher())
            mContext.startActivity(intentComment)
        }
        Picasso.get().load(user.getImage()).placeholder(R.drawable.placeholderfour).into(holder.im)
    }

    override fun getItemCount(): Int {
        return mKaba.size
    }
    fun clear() {
        //feed is your model class
        mKaba.clear()
        notifyDataSetChanged()
    }

    fun addAll(kaba: MutableList<Forum>?) {
        kaba!!.addAll(kaba)
    }


    class ViewHolder(@NonNull itemView : View): RecyclerView.ViewHolder(itemView)
    {
        var fullnametextview: TextView = itemView.findViewById(R.id.faraname)
        var mobiletextview: TextView = itemView.findViewById(R.id.faranametwo)
        var im: ImageView = itemView.findViewById(R.id.fora)


    }
}