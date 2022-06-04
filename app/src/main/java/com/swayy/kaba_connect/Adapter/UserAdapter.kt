package com.swayy.kaba_connect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.CustomBottomSheetDialogFragment
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.User
import de.hdodenhof.circleimageview.CircleImageView


class UserAdapter (private var mContext: Context,
                   private  var mUser: List<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view  = LayoutInflater.from(mContext).inflate(R.layout.fragment_profile, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]
        holder.fullnametextview.text = user.getFullname()
        holder.emailtextview.text = user.getEmail()
        holder.mobiletextview.text = user.getMobile()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.placeholderthree).into(holder.im)

    }

    class ViewHolder(@NonNull itemView : View): RecyclerView.ViewHolder(itemView)
    {
        var fullnametextview: TextView = itemView.findViewById(R.id.etFirstNameone)
        var emailtextview: TextView = itemView.findViewById(R.id.etEmailone)
        var mobiletextview: TextView = itemView.findViewById(R.id.etLastNameone)
        var im:ImageView = itemView.findViewById(R.id.profile_image_settings)






    }


}