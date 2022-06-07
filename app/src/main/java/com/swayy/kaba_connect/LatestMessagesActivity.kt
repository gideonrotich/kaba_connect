package com.swayy.kaba_connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.model.ChatMessage
import com.swayy.kaba_connect.model.Userr
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.latest_message_row.view.*
/**
 * Code written by Gideon Rotich
 */
class LatestMessagesActivity : AppCompatActivity() {

    companion object {
        var currentUser: Userr? = null
        val TAG = "LatestMessages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        boli.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        recyclerview_latest_messages.adapter = adapter
//        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG,"123")
            val intent = Intent(this,ChatLogActivity::class.java)

            val row = item as LatestMessageRow

            intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }

//        setupDummyRows()
        listenForLatestMessages()
        fetchCurrentUser()

        add_messagev.setOnClickListener {
            val intent = Intent(this,NewMessageActivity::class.java)
            startActivity(intent)
        }



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
        ref.addChildEventListener(object: ChildEventListener{
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
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(Userr::class.java)
                Log.d("latestmessages","current user ${currentUser!!.fullname}")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}