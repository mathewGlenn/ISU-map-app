package com.isucollegemapadmin

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
import com.isucollegemapadmin.databinding.ActivityFacultyListBinding

class ListFaculty : AppCompatActivity() {

    lateinit var facultyList: RecyclerView

    lateinit var fireStore: FirebaseFirestore
    lateinit var entryAdapter: FirestoreRecyclerAdapter<Faculty, ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFacultyListBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        facultyList = binding.facultyList
        fireStore = FirebaseFirestore.getInstance()

        val collegeName = binding.collegeName
        val college = intent.getStringExtra("college").toString()
        when (college) {
            "CCSICT" -> {

                collegeName.text = resources.getString(R.string.ccsict)
            }
            "IAT" -> {

                collegeName.text = resources.getString(R.string.iat)
            }

            "SAS" -> {

                collegeName.text = resources.getString(R.string.sas)
            }
            "CBM" -> {

                collegeName.text = resources.getString(R.string.cbm)
            }
            "COE" -> {

                collegeName.text = resources.getString(R.string.coe)
            }
            "CCJE" -> {

                collegeName.text = resources.getString(R.string.ccje)
            }
            "PS" -> {

                collegeName.text = resources.getString(R.string.ps)
            }
        }

        val query: Query =
            fireStore.collection(college).document("faculty").collection("faculty-list")

        val allFaculty = FirestoreRecyclerOptions.Builder<Faculty>()
            .setQuery(query, Faculty::class.java)
            .build()

        entryAdapter =
            object :
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


                    //get id of faculty to be used in updating and deleting
                    val facultyID = entryAdapter.snapshots.getSnapshot(i).id

                    // When clicking an Entry from the RecylerView entry list
                    viewHolder.view.setOnClickListener { v: View ->
                        val i1 = Intent(v.context, FacultyProfile::class.java)
                        //get the title, content, date, etc to view in the EntryContent.class
                        i1.putExtra("facultyID", facultyID)
                        i1.putExtra("collegeAcronym", college)
                        i1.putExtra("name", facultyName)
                        i1.putExtra("position", facultyPosition)
                        i1.putExtra("imgLink", facultyImgLink)
                        i1.putExtra("college", collegeName.text.toString())
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

        binding.buttonAddFaculty.setOnClickListener{
            startActivity(Intent(this, AddFaculty::class.java).putExtra("college", college))
        }

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