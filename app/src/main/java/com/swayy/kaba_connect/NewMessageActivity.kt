package com.swayy.kaba_connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.model.Userr
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        tchop.setOnClickListener {
            startActivity(Intent(this,LatestMessagesActivity::class.java))
        }

//        val adapter  = GroupAdapter<ViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        recyclerview_newmessage.adapter = adapter



        fetchUsers()

    }
    companion object {
        val USER_KEY = "USER_KEY"
//        val USER_KEY_IMAGE = "USER_KEY_IMAGE"
//        val USER_KEY_PROFESSION = "USER_KEY_PROFESSION"
//        val USER_KEY_UID = "USER_KEY_UID"
    }

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/Users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage",it.toString())
                    val userr = it.getValue(Userr::class.java)
                    if (userr != null){
                        adapter.add(UserItem(userr))
                    }

                }

                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem

                    val intent = Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.userr)
//                    intent.putExtra(USER_KEY_IMAGE,userItem.user.getImage())
//                    intent.putExtra(USER_KEY_PROFESSION,userItem.user.getProfession())
//                    intent.putExtra(USER_KEY_UID,userItem.user.getUid())
                    startActivity(intent)
                    finish()
                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

class UserItem(val userr: Userr): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview.text = userr.fullname
        Picasso.get().load(userr.image).placeholder(R.drawable.twoo).into(viewHolder.itemView.image_list)

    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}


//
//class CustomAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
//    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
//
//    }
//
//}