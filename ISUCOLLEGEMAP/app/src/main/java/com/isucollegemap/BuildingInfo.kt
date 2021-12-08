package com.isucollegemap

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class BuildingInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_info)

        val buildingImage = findViewById<ImageView>(R.id.buildingImg)
        val buildingName = findViewById<TextView>(R.id.buildingName)


        val intentBldgName = intent.getStringExtra("building").toString()

        //get building name and image from firestore
        FirebaseFirestore.getInstance().collection("Buildings").document("building-list").collection(intentBldgName)
            .document("building-name")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val imgLink = it.getString("img_link").toString()
                    buildingName.text = it.getString("building_name").toString()
                    if (imgLink != "no_image") {
                        Glide.with(this)
                            .load(imgLink)
                            .into(buildingImage)
                    }
                }
            }
    }
}