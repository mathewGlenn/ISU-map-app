package com.isucollegemapadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.isucollegemapadmin.databinding.ActivityMapBinding

class Map : AppCompatActivity(), View.OnClickListener{
    lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
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

    override fun onClick(p0: View?) {
        when(p0){
            binding.viewIAT ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "iat"))
            binding.viewAutoMobileRepairBldg ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "autoMobileRepairBldg"))
            binding.viewAthleticGround1part ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "athleticGround"))
            binding.viewAthleticGround2part ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "athleticGround"))
            binding.viewFoodCourt ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "foodCourt"))
            binding.viewCoESecondary ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "coeSecondary"))
            binding.viewCoeElementary ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "coeElementary"))
            binding.viewCcsictBldg ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "ccsictBldg"))
            binding.viewRamonMagsaysayBldg ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "ramonMagsaysayBldg"))
            binding.viewSAS ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "sas"))
            binding.viewPS ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "ps"))
            binding.view3StoreyLibrary ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "library"))
            binding.viewCBM ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "cbm"))
            binding.viewCbmBldg ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "cbmBldg"))
            binding.viewGuardHouse ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "guardHouse"))
            binding.viewCharMBldg ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "charMBldg"))
            binding.viewDipsCenter ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "DispCenter"))
            binding.viewAdminBldg ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "adminBldg"))
            binding.viewHostel ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "hostel"))
            binding.viewNstpBldg ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "nstpbldg"))
            binding.viewSacBldg1part ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "sacBldg"))
            binding.viewSacBldg2part ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "sacBldg"))
            binding.viewCrimLaboratory ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "crimLab"))
            binding.viewGymnasium ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "Gymnasium"))
            binding.viewClinic ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "clinic"))
            binding.viewOldStage ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "oldStage"))
            binding.viewCcjeClassrooms ->
                startActivity(Intent(this, EditBuildingInfo::class.java)
                    .putExtra("building", "ccjeClassrooms"))
        }
    }
}