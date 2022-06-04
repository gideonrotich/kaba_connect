package com.swayy.kaba_connect.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.ChatLogActivity
import com.swayy.kaba_connect.LatestMessagesActivity
import com.swayy.kaba_connect.NewMessageActivity
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.ChatMessage
import com.swayy.kaba_connect.model.User
import com.swayy.kaba_connect.model.Userr
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_message.view.*
import kotlinx.android.synthetic.main.latest_message_row.view.*


class messageFragment : Fragment() {
    private lateinit var firebaseUser: FirebaseUser
    companion object {
        var currentUser: Userr? = null
        val TAG = "LatestMessages"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_message, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        view.recyclerview_latest_messagesa.adapter = adapter
//        view.recyclerview_latest_messagesa.addItemDecoration(
//            DividerItemDecoration(context,
//                DividerItemDecoration.VERTICAL)
//        )
        adapter.setOnItemClickListener { item, view ->
            Log.d(LatestMessagesActivity.TAG,"123")
            val intent = Intent(context, ChatLogActivity::class.java)

            val row = item as messageFragment.LatestMessageRow

            intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }

//        setupDummyRows()
        listenForLatestMessages()
        fetchCurrentUser()
        //userinfo()

        view.add_message.setOnClickListener {
            val intent = Intent(context, NewMessageActivity::class.java)
            startActivity(intent)
        }



        return view
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, previousChildName: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()


            }
            override fun onChildChanged(p0: DataSnapshot, previousChildName: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
        var chatPartnerUser:Userr? = null
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
            ref.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser = p0.getValue(Userr::class.java)
                    viewHolder.itemView.username_textview_latest_message.text = chatPartnerUser!!.fullname
                    val targetImageView = viewHolder.itemView.imageview_latest_message
                    Picasso.get().load(chatPartnerUser!!.image).placeholder(R.drawable.twoo).into(targetImageView)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
        override fun getLayout(): Int {
            return R.layout.latest_message_row
        }
    }

    val adapter = GroupAdapter<ViewHolder>()

//    private fun setupDummyRows() {
//
//
//        adapter.add(LatestMessageRow())
//        adapter.add(LatestMessageRow())
//        adapter.add(LatestMessageRow())
//
//
//    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(Userr::class.java)
                Log.d("latestmessages","current user ${currentUser!!.fullname}")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun userinfo() {
        val jobsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)

        jobsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Glide.with(context!!)  //2
                        .load(user!!.getImage()) //3
                        .centerCrop() //4
                        .into(view!!.profodr)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}