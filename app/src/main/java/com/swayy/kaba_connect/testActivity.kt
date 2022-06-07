package com.swayy.kaba_connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
/**
 * Code written by Gideon Rotich
 */
class testActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val ref = FirebaseDatabase.getInstance().getReference("/Users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
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

                    val intent = Intent(this@testActivity,ChatLogActivity::class.java)
                    intent.putExtra(testActivity.USER_KEY,userItem.userr)
//                    intent.putExtra(USER_KEY_IMAGE,userItem.user.getImage())
//                    intent.putExtra(USER_KEY_PROFESSION,userItem.user.getProfession())
//                    intent.putExtra(USER_KEY_UID,userItem.user.getUid())
                    startActivity(intent)
                    finish()
                }

                recyclerview_newmessaged.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
        val USER_KEY = "USER_KEY"

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

}