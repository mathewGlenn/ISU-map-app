package com.isucollegemapadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.isucollegemapadmin.databinding.ActivityCollegesBinding


class Colleges : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCollegesBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.ccsict.setOnClickListener{
            startActivity(Intent(this, ListFaculty::class.java)
                .putExtra("college", "CCSICT"))
        }

        binding.ccje.setOnClickListener{
            startActivity(Intent(this, ListFaculty::class.java)
                .putExtra("college", "CCJE"))
        }
        binding.cbm.setOnClickListener{
            startActivity(Intent(this, ListFaculty::class.java)
                .putExtra("college", "CBM"))
        }
        binding.coe.setOnClickListener{
            startActivity(Intent(this, ListFaculty::class.java)
                .putExtra("college", "COE"))
        }
        binding.iat.setOnClickListener{
            startActivity(Intent(this, ListFaculty::class.java)
                .putExtra("college", "IAT"))
        }
        binding.ps.setOnClickListener{
            startActivity(Intent(this, ListFaculty::class.java)
                .putExtra("college", "PS"))
        }
        binding.sas.setOnClickListener{
            startActivity(Intent(this, ListFaculty::class.java)
                .putExtra("college", "SAS"))
        }

    }
}