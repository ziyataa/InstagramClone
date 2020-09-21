package com.example.instagramclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.R
import com.example.instagramclone.adapter.CommentAdapter
import com.example.instagramclone.model.Comment
import com.example.instagramclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {

    private var postId : String? = null
    private var publisherId : String? = null
    private var firebaseUser: FirebaseUser? = null
    private var commentAdapter: CommentAdapter? = null
    private var commentList: MutableList<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val intent = intent
        postId = intent.getStringExtra("postId")
        publisherId = intent.getStringExtra("publisherId")

        firebaseUser = FirebaseAuth.getInstance().currentUser

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recyclerview_comments)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        recyclerView.layoutManager = linearLayoutManager

        commentList = ArrayList()
        commentAdapter = CommentAdapter(this, commentList as ArrayList<Comment>)
        recyclerView.adapter = commentAdapter

        userInfo()
        readComment() //mengambil semua komen user
        getPostImageComment() //menampilkan post image di menu comment

        txtpost_comments.setOnClickListener {
            if (add_comment!!.text.toString() == "") {
                Toast.makeText(
                    this@CommentActivity,
                    "Please write comment first",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                addComment()
            }
        }
    }

    private fun readComment() {
        var commentsRef = postId?.let {
            FirebaseDatabase.getInstance().reference.child("Comments").child(
                it
            )
        }

        commentsRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    commentList!!.clear()

                    for (snapshot in p0.children){
                        val comment = snapshot.getValue(Comment::class.java)
                        commentList!!.add(comment!!)

                    }
                    commentAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    private fun getPostImageComment() {
        val postCommentRef = postId?.let {
            FirebaseDatabase.getInstance().reference
                .child("Posts").child(it).child("postImage")
        }

        postCommentRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val image = p0.value.toString()

                    Picasso.get().load(image).placeholder(R.drawable.ic_profile)
                        .into(post_image_comment)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    private fun addComment() {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments").child(postId!!)

        val commentsMap = HashMap<String, Any>()
        commentsMap["comment"] = add_comment!!.text.toString()
        commentsMap["publisher"] = firebaseUser!!.uid

        commentsRef.push().setValue(commentsMap)

        add_comment!!.text.toString()

    }

    private fun userInfo() {
        val usersRef = FirebaseDatabase.getInstance().reference
            .child("Users").child(firebaseUser!!.uid)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.ic_profile)
                        .into(profile_image_comment)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}