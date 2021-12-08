package com.isucollegemapadmin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.isucollegemapadmin.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        var userName = ""
        var password = ""

        val fireStore = FirebaseFirestore.getInstance()

        val docRef: DocumentReference = fireStore.collection("Admin user").document("user-cred")
        docRef.get()
            .addOnSuccessListener {
                if (it.exists()){
                    userName = it.getString("username").toString()
                    password = it.getString("password").toString()
                }
            }

        binding.buttonLogin.setOnClickListener{
            binding.progress.visibility = View.VISIBLE
            if ((binding.editPassword.text.toString() == password) and
                (binding.editUsername.text.toString() == userName)
            ){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else{
                binding.progress.visibility = View.INVISIBLE
                Snackbar.make(binding.parentLayout, "Wrong credentials", Snackbar.LENGTH_SHORT).show()
            }


        }




    }
}