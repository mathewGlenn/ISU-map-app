package com.isucollegemap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.Query
import com.isucollegemap.databinding.ActivityCollegeInfoBinding
import com.isucollegemap.model.Faculty

open class CollegeInfo : AppCompatActivity() {

    lateinit var facultyList: RecyclerView

    lateinit var fireStore: FirebaseFirestore
    lateinit var entryAdapter: FirestoreRecyclerAdapter<Faculty, ViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCollegeInfoBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        facultyList = binding.facultyList
        fireStore = FirebaseFirestore.getInstance()

        val buildingImage = binding.buildingImg
        val college = binding.collegeName
        val buildingName = binding.buildingName
        var collegeAcronym = "OTHER"

        val intentBldgName = intent.getStringExtra("building").toString()

        //get building name and image from firestore
        fireStore.collection("Buildings").document("building-list").collection(intentBldgName)
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

        when (intentBldgName) {
            "iat" -> {
                collegeAcronym = "IAT"
                college.text = resources.getString(R.string.iat)
            }
            "autoMobileRepairBldg" -> {

                collegeAcronym = "PS"
                college.text = resources.getString(R.string.ps)

            }
            "ccsictBldg" -> {

                collegeAcronym = "CCSICT"
                college.text = resources.getString(R.string.ccsict)

            }
            "ramonMagsaysayBldg" -> {

                collegeAcronym = "CCSICT"
                college.text = resources.getString(R.string.ccsict)

            }
            "sas" -> {

                collegeAcronym = "SAS"
                college.text = resources.getString(R.string.sas)

            }
            "cbm" -> {

                collegeAcronym = "CBM"
                college.text = resources.getString(R.string.cbm)

            }
            "cbmBldg" -> {

                collegeAcronym = "CBM"
                college.text = resources.getString(R.string.cbm)

            }
            "coeElementary" -> {

                collegeAcronym = "COE"
                college.text = resources.getString(R.string.coe)

            }
            "coeSecondary" -> {

                collegeAcronym = "COE"
                college.text = resources.getString(R.string.coe)

            }
            "ps" -> {

                collegeAcronym = "PS"
                college.text = resources.getString(R.string.ps)

            }

            "crimLab" -> {

                collegeAcronym = "CCJE"
                college.text = resources.getString(R.string.ccje)

            }
            "ccjeClassrooms" -> {

                collegeAcronym = "CCJE"
                college.text = resources.getString(R.string.ccje)

            }
        }

        val query: Query =
            fireStore.collection(collegeAcronym).document("faculty").collection("faculty-list")
                .orderBy("position")

        val allFaculty = FirestoreRecyclerOptions.Builder<Faculty>()
            .setQuery(query, Faculty::class.java)
            .build()

        entryAdapter = object :
            FirestoreRecyclerAdapter<Faculty, ViewHolder>(
                allFaculty) {
            override fun onBindViewHolder(
                viewHolder: ViewHolder,
                i: Int,
                faculty: Faculty,
            ) {

                val facultyName: String = faculty.name
                val facultyPosition: String = faculty.position
                val facultyImgLink: String = faculty.img_link

                viewHolder.facultyName.text = faculty.name
                viewHolder.facultyPosition.text = faculty.position

                if (facultyImgLink != "no_image") {
                    Glide.with(applicationContext).load(facultyImgLink)
                        .into(viewHolder.facultyImage)
                } else {
                    viewHolder.facultyImage.setImageResource(R.drawable.image_placeholder)
                }


                //get id of entry to be used in updating and deleting
                val docID = entryAdapter.snapshots.getSnapshot(i).id

                // When clicking an Entry from the RecylerView entry list
                viewHolder.view.setOnClickListener { v: View ->
                    val i1 = Intent(v.context, FacultyProfile::class.java)

                    i1.putExtra("name", faculty.name)
                    i1.putExtra("position", faculty.position)
                    i1.putExtra("imgLink", faculty.img_link)
                    i1.putExtra("college", college.text.toString())
                    startActivity(i1)
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int,
            ): ViewHolder {
                val view1: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.faculty_view, parent, false)
                return ViewHolder(view1)
            }
        }

        facultyList.layoutManager = LinearLayoutManager(this)
        facultyList.adapter = entryAdapter

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var facultyName: TextView = itemView.findViewById(R.id.facultyname)
        var facultyPosition: TextView = itemView.findViewById(R.id.facultyPosition)
        var facultyImage: ImageView = itemView.findViewById(R.id.facultyImg)
        var view: View = itemView

    }

    override fun onStart() {
        super.onStart()
        entryAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        entryAdapter.stopListening()
    }
}