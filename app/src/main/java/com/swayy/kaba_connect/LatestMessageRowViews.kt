package com.swayy.kaba_connect


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.model.ChatMessage
import com.swayy.kaba_connect.model.Userr
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latest_message.text = chatMessage.text
        val chatPartnerId:String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid)
        {
            chatPartnerId = chatMessage.toId
        }
        else{
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/Users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Userr::class.java)
                viewHolder.itemView.username_textview_latest_message.text = user!!.fullname
                val targetImageView = viewHolder.itemView.imageview_latest_message
                Picasso.get().load(user.image).placeholder(R.drawable.twoo).into(targetImageView)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}