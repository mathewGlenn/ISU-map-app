package com.isucollegemapadmin

import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.isucollegemapadmin.databinding.ActivityEditBuildingInfoBinding
import java.io.*
import java.util.*

class EditBuildingInfo : AppCompatActivity() {


    val APP_TAG = "crop"
    var intermediateName = "1.jpg"
    var resultName = "2.jpg"
    var intermediateProvider: Uri? = null
    var resultProvider: Uri? = null
    var galleryActivityResultLauncher: ActivityResultLauncher<Intent>? = null
    var cropActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    var isBuildingImagePresent: Boolean = false

    lateinit var firestore: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    lateinit var imageBitmap: Bitmap
    lateinit var buildingName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditBuildingInfoBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        buildingName = intent.getStringExtra("building").toString()
        val editBuildingName = binding.editBuildingName
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val documentReference: DocumentReference =
            firestore.collection("Buildings").document("building-list").collection(buildingName)
                .document("building-name")

        documentReference.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val imgLink = it.getString("img_link").toString()
                    editBuildingName.setText(it.getString("building_name").toString())
                    if (imgLink != "no_image") {
                        Glide.with(this)
                            .load(imgLink)
                            .into(binding.image)
                    }
                } else {
                    editBuildingName.setText(buildingName)
                }
            }


        binding.chooseImage.setOnClickListener {
            onPickPhoto()
        }

        galleryActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                saveBitmapFileToIntermediate(result.data!!.data)
                onCropImage()
            }
        }
        cropActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val cropImage = loadFromUri(resultProvider)
                imageBitmap = getResizedBitmap(cropImage, 1000)
                binding.image.setImageBitmap(imageBitmap)
                isBuildingImagePresent = true
            }
        }


        //add faculty
        binding.buttonSave.setOnClickListener {

            if (isBuildingImagePresent) {
                val imageName = UUID.randomUUID().toString()
                val storReference =
                    storageReference.child("images/buildings/$buildingName/$imageName")

                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading image...")
                progressDialog.show()

                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data: ByteArray = baos.toByteArray()

                val uploadTask: UploadTask = storReference.putBytes(data)

                uploadTask.addOnProgressListener { snapshot: UploadTask.TaskSnapshot ->
                    val progress =
                        100.0 * snapshot.bytesTransferred / snapshot.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    storReference.downloadUrl
                }.addOnCompleteListener { task: Task<Uri> ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val building: MutableMap<String, Any> = HashMap()
                        building["building_name"] = binding.editBuildingName.text.toString()
                        building["img_link"] = downloadUri.toString()
                        documentReference.set(building, SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext,
                                    "Save successful",
                                    Toast.LENGTH_LONG).show()
                                progressDialog.dismiss()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext,
                                    "Save failed",
                                    Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this,
                            task.exception.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val building: MutableMap<String, Any> = HashMap()
                building["building_name"] = binding.editBuildingName.text.toString()
                building["img_link"] = "no_image"
                documentReference.set(building, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext,
                            "Save successful",
                            Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext,
                            "Save failed",
                            Toast.LENGTH_LONG).show()
                    }
            }


        }
    }


    // Trigger gallery selection for a photo
    fun onPickPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            galleryActivityResultLauncher!!.launch(intent)
        }
    }

    private fun onCropImage() {
        grantUriPermission(
            "com.android.camera",
            intermediateProvider,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(intermediateProvider, "image/*")

        val list = packageManager.queryIntentActivities(intent, 0)

        grantUriPermission(
            list[0].activityInfo.packageName,
            intermediateProvider,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION

        )

        val size: Int = list.size
        if (size == 0) {
            Toast.makeText(this, "Error: No image taken!", Toast.LENGTH_SHORT).show()
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 0)
            intent.putExtra("aspectY", 0)
            intent.putExtra("scale", true);
            val photoFile = getPhotoFileUri(resultName)
            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            resultProvider = FileProvider.getUriForFile(
                this,
                "com.isumap.crop.fileprovider",
                photoFile
            )
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, resultProvider)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            val cropIntent = Intent(intent)
            val res = list[0]
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            grantUriPermission(
                res.activityInfo.packageName,
                resultProvider,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            cropIntent.component =
                ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            cropActivityResultLauncher!!.launch(cropIntent)
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        val mediaStorageDir = File(getExternalFilesDir(""), APP_TAG)
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                // on newer versions of Android, use the new decodeBitmap method
                val source = ImageDecoder.createSource(
                    this.contentResolver,
                    photoUri!!
                )
                ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    private fun saveBitmapFileToIntermediate(sourceUri: Uri?) {
        try {
            val bitmap = loadFromUri(sourceUri)
            val imageFile = getPhotoFileUri(intermediateName)
            intermediateProvider =
                FileProvider.getUriForFile(
                    this,
                    "com.isumap.crop.fileprovider",
                    imageFile
                )
            val out: OutputStream = FileOutputStream(imageFile)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getResizedBitmap(image: Bitmap?, maxSize: Int): Bitmap {
        var width = image!!.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}