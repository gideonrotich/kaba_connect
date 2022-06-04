package com.swayy.kaba_connect.Forum

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.swayy.kaba_connect.MainActivity
import com.swayy.kaba_connect.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_createforum.*
import kotlinx.android.synthetic.main.activity_profile.*

class CreateforumActivity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createforum)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("Forum Pictures")
        save_new_post_forum.setOnClickListener {
            uploadforum()
        }


        change_image_forum.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(12,8)
                .start(this@CreateforumActivity)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data!= null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_image_settings_forum.setImageURI(imageUri)

        }
    }


    private fun uploadforum() {
        when{

            imageUri == null -> Toast.makeText(this,"Please select image first",Toast.LENGTH_LONG).show()
            etEmaillforum.text.toString() == "" -> {
                Toast.makeText(this,"Write forum name first", Toast.LENGTH_LONG).show()}

            etEmailldescrip.text.toString() == "" -> {
                Toast.makeText(this,"Write forum description first", Toast.LENGTH_LONG).show()}
            else -> {
                val progressDialog  = ProgressDialog(this)
                progressDialog.setTitle("Publishing Forum")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.setMessage("Please wait,this might take a while...")
                progressDialog.show()

                val fileRef  = storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref  = FirebaseDatabase.getInstance().reference.child("Forum")
                        val postId = ref.push().key

                        val userMap = HashMap<String, Any>()
                        userMap["forumId"] = postId!!
                        userMap["name"] = etEmaillforum.text.toString().toLowerCase()
                        userMap["description"] = etEmailldescrip.text.toString().toLowerCase()
                        userMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        userMap["image"] = myUrl

                        ref.child(postId).updateChildren(userMap)
                        //notification
                        val notRef = FirebaseDatabase.getInstance().reference.child("Notifications").child(firebaseUser.uid)

                        val commentsMap = HashMap<String, Any>()
                        commentsMap["notification"] = "Your forum is live"
                        commentsMap["publisher"] = firebaseUser!!.uid
                        commentsMap["image"] = myUrl

                        notRef.push().setValue(commentsMap)

                        Toast.makeText(this,"Forum has been uploaded successfully", Toast.LENGTH_LONG).show()

                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                    else
                    {
                        progressDialog.dismiss()
                    }
                } )
            }
        }
    }


}