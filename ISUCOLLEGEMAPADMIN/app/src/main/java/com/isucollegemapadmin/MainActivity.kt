package com.isucollegemapadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.isucollegemapadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.colleges.setOnClickListener{
            startActivity(Intent(this, Colleges::class.java))
        }
        binding.map.setOnClickListener{
            startActivity(Intent(this, Map::class.java))
        }
    }
}