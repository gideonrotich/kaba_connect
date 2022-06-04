package com.swayy.kaba_connect.Adapter

import android.content.Context
import android.content.Intent
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.swayy.kaba_connect.CommentsActivity
import com.swayy.kaba_connect.CustomBottomSheetDialogFragment
import com.swayy.kaba_connect.MainActivity
import com.swayy.kaba_connect.R
import com.swayy.kaba_connect.model.Post
import com.swayy.kaba_connect.model.User
import com.swayy.kaba_connect.model.Verified
import de.hdodenhof.circleimageview.CircleImageView


class PostAdapter(private var mContext:Context,
                  private val mPost:ArrayList<Post>,
                  private val mVerified: List<Verified>): RecyclerView.Adapter<PostAdapter.ViewHolder>()
{
    private var firebaseUser:FirebaseUser? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mPost.size

    }

    //create two methods for clearing and adding data to your adapter /am not good at kotlin so lemi copy

    fun clear() {
        //feed is your model class
        mPost.clear()
        notifyDataSetChanged()
    }

    fun addAll(posts: MutableList<Post>?) {
        posts!!.addAll(posts)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       firebaseUser = FirebaseAuth.getInstance().currentUser

        val post = mPost[position]

        if(post.getPostimage() == ""){
            holder.postImage.visibility = View.GONE
        }
       if(post.getPostimage() != ""){
           holder.postImage.visibility = View.VISIBLE
            Picasso.get().load(post.getPostimage()).into(holder.postImage)
       }


        if (post.getDescription().equals(""))
        {
            holder.description.visibility = View.GONE
        }
        else{
            holder.description.visibility = View.VISIBLE
            holder.description.setText(post.getDescription())
        }

        publisherInfo(holder.profileImage, holder.userName,post.getPublisher(),holder.profession,holder.verification,holder.userNamee)

        isLikes(post.getPostid(), holder.likeButton)

        numberOfLikes(holder.likes, post.getPostid())

        getTotalComments(holder.comments, post.getPostid())

        holder.menu.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                mContext,R.style.BottomSheetDialogTheme
            )
            val bottomSheetView = LayoutInflater.from(mContext).inflate(
                R.layout.bottom_sheet,null)


            bottomSheetView.findViewById<View>(R.id.buttonShare).setOnClickListener {
                Toast.makeText(mContext,"Post will be removed from your feed..", Toast.LENGTH_LONG).show()
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }





        holder.likeButton.setOnClickListener {
            if (holder.likeButton.tag == "Like")
            {
                FirebaseDatabase.getInstance().reference
                    .child("Likes")
                    .child(post.getPostid())
                    .child(firebaseUser!!.uid)
                    .setValue(true)
            }
            else{
                FirebaseDatabase.getInstance().reference
                    .child("Likes")
                    .child(post.getPostid())
                    .child(firebaseUser!!.uid)
                    .removeValue()
                val intent = Intent(mContext, MainActivity::class.java)
                mContext.startActivity(intent)
            }
        }

        holder.shareButton.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out this  in the swayy app\n" +
                        post.getDescription())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            mContext.startActivity(shareIntent)


        }

        holder.commentButton.setOnClickListener {
            val intentComment = Intent(mContext, CommentsActivity::class.java)
            intentComment.putExtra("postId", post.getPostid())
            intentComment.putExtra("publisherId", post.getPublisher())
            mContext.startActivity(intentComment)
        }
        holder.profileImage.setOnClickListener {
            val pref = mContext?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",post.getPublisher())
            pref?.apply()
            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            modalbottomSheetFragment.show((mContext as FragmentActivity).supportFragmentManager,modalbottomSheetFragment.tag)
        }
        holder.userName.setOnClickListener {
            val pref = mContext?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
            pref?.putString("userid",post.getPublisher())
            pref?.apply()
            val modalbottomSheetFragment = CustomBottomSheetDialogFragment()
            modalbottomSheetFragment.show((mContext as FragmentActivity).supportFragmentManager,modalbottomSheetFragment.tag)
        }




        holder.comments.setOnClickListener {
            val intentComment = Intent(mContext,CommentsActivity::class.java)
            intentComment.putExtra("postId", post.getPostid())
            intentComment.putExtra("publisherId", post.getPublisher())
            mContext.startActivity(intentComment)
        }
        holder.description.setOnClickListener {
            val intentComment = Intent(mContext,CommentsActivity::class.java)
            intentComment.putExtra("postId", post.getPostid())
            intentComment.putExtra("publisherId", post.getPublisher())
            mContext.startActivity(intentComment)
        }
    }

    private fun numberOfLikes(likes: TextView, postid: String) {
        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(postid)

        LikesRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    likes.text = p0.childrenCount.toString() + ""
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun getTotalComments(comments: TextView, postid: String) {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments").child(postid)

        commentsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    comments.text = "View all " + p0.childrenCount.toString() + " comments"
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun isLikes(postid: String, likeButton: ImageView) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(postid)

        LikesRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(firebaseUser!!.uid).exists())
                {
                   likeButton.setImageResource(R.drawable.ic_mimi_liked)
                    likeButton.tag = "Liked"
                }
                else
                {
                    likeButton.setImageResource(R.drawable.ic_mimi_like)
                    likeButton.tag = "Like"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }


    inner class ViewHolder(@NonNull itemView:View): RecyclerView.ViewHolder(itemView)
    {
        var profileImage: CircleImageView
        var postImage: ImageView
        var likeButton: ImageView
        var commentButton: ImageView
        var shareButton: ImageView
        var userName: TextView
        var userNamee: TextView
        var likes: TextView
        var profession:TextView
        var description: TextView
        var comments: TextView
        var menu: ImageView
        var verification:ImageView






        init {
            profileImage  = itemView.findViewById(R.id.pichayako)
            postImage = itemView.findViewById(R.id.picture_post)
            likeButton = itemView.findViewById(R.id.pichalike)
            commentButton = itemView.findViewById(R.id.pichacomment)
            shareButton = itemView.findViewById(R.id.pichashare)
            userName = itemView.findViewById(R.id.fullname_post)
            likes = itemView.findViewById(R.id.likes)
            profession = itemView.findViewById(R.id.proffesion_post)
            description = itemView.findViewById(R.id.text_post)
            comments = itemView.findViewById(R.id.totalcomments)
            menu = itemView.findViewById(R.id.menusheet)
            verification = itemView.findViewById(R.id.verification)
            userNamee = itemView.findViewById(R.id.fullname_posta)



        }

    }

    private fun publisherInfo(profileImage: CircleImageView, userName: TextView,  publisherID: String, profession:TextView,verification:ImageView,userNamee: TextView) {

        val  usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherID)

        usersRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists())
                {
                    val user = p0.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.placeholderthree).into(profileImage)
                    userName.text = user!!.getFullname()
                    profession.text = user!!.getMobile()
                    userNamee.text = "@"+user!!.getFullname()
                    if(user.getFullname() == "gideon rotich")
                    {
                        verification.visibility = View.VISIBLE
                    }
                    if(user.getFullname() != "gideon rotich")
                    {
                        verification.visibility = View.GONE
                    }







                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}