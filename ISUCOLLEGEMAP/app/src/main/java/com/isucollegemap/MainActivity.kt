package com.isucollegemap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.isucollegemap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.viewIAT.setOnClickListener(this)
        binding.viewAutoMobileRepairBldg.setOnClickListener(this)
        binding.viewAthleticGround1part.setOnClickListener(this)
        binding.viewAthleticGround2part.setOnClickListener(this)
        binding.viewFoodCourt.setOnClickListener(this)
        binding.viewCoESecondary.setOnClickListener(this)
        binding.viewCoeElementary.setOnClickListener(this)
        binding.viewCcsictBldg.setOnClickListener(this)
        binding.viewRamonMagsaysayBldg.setOnClickListener(this)
        binding.viewSAS.setOnClickListener(this)
        binding.viewPS.setOnClickListener(this)
        binding.view3StoreyLibrary.setOnClickListener(this)
        binding.viewCBM.setOnClickListener(this)
        binding.viewCbmBldg.setOnClickListener(this)
        binding.viewGuardHouse.setOnClickListener(this)
        binding.viewCharMBldg.setOnClickListener(this)
        binding.viewDipsCenter.setOnClickListener(this)
        binding.viewAdminBldg.setOnClickListener(this)
        binding.viewHostel.setOnClickListener(this)
        binding.viewNstpBldg.setOnClickListener(this)
        binding.viewSacBldg1part.setOnClickListener(this)
        binding.viewSacBldg2part.setOnClickListener(this)
        binding.viewCrimLaboratory.setOnClickListener(this)
        binding.viewGymnasium.setOnClickListener(this)
        binding.viewClinic.setOnClickListener(this)
        binding.viewOldStage.setOnClickListener(this)
        binding.viewCcjeClassrooms.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view) {
            binding.viewIAT ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "iat"))
            binding.viewAutoMobileRepairBldg ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "autoMobileRepairBldg"))
            binding.viewAthleticGround1part ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "athleticGround"))
            binding.viewAthleticGround2part ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "athleticGround"))
            binding.viewFoodCourt ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "foodCourt"))
            binding.viewCoESecondary ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "coeSecondary"))
            binding.viewCoeElementary ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "coeElementary"))
            binding.viewCcsictBldg ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "ccsictBldg"))
            binding.viewRamonMagsaysayBldg ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "ramonMagsaysayBldg"))
            binding.viewSAS ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "sas"))
            binding.viewPS ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "ps"))
            binding.view3StoreyLibrary ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "library"))
            binding.viewCBM ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "cbm"))
            binding.viewCbmBldg ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "cbmBldg"))
            binding.viewGuardHouse ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "guardHouse"))
            binding.viewCharMBldg ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "charMBldg"))
            binding.viewDipsCenter ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "DispCenter"))
            binding.viewAdminBldg ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "adminBldg"))
            binding.viewHostel ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "hostel"))
            binding.viewNstpBldg ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "nstpbldg"))
           // binding.viewSacBldg1part ->
                //startActivity(Intent(this, CollegeInfo::class.java)
                   // .putExtra("building", "sacBldg"))
           // binding.viewSacBldg2part ->
                //startActivity(Intent(this, EditBuildingInfo::class.java)
                    //.putExtra("building", "sacBldg"))
            binding.viewCrimLaboratory ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "crimLab"))
            binding.viewGymnasium ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "Gymnasium"))
            binding.viewClinic ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "clinic"))
            binding.viewOldStage ->
                startActivity(Intent(this, BuildingInfo::class.java)
                    .putExtra("building", "oldStage"))
            binding.viewCcjeClassrooms ->
                startActivity(Intent(this, CollegeInfo::class.java)
                    .putExtra("building", "ccjeClassrooms"))


        }
    }
}