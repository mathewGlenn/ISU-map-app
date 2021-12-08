package com.isucollegemap

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.isucollegemap.databinding.ActivityFacultyProfileBinding

class FacultyProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFacultyProfileBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val imagelink = intent.getStringExtra("imgLink").toString()
        val name = intent.getStringExtra("name").toString()
        val position = intent.getStringExtra("position").toString()
        val college = intent.getStringExtra("college").toString()

        if (imagelink != "no_image") {
            Glide.with(this)
                .load(imagelink)
                .into(binding.image)
        }
        binding.facultyName.text = name
        binding.facultyPosition.text = position
        binding.college.text = college

    }
}