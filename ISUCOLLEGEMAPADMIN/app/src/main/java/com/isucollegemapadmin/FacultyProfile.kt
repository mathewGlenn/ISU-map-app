package com.isucollegemapadmin


import android.app.ProgressDialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.isucollegemapadmin.databinding.ActivityFacultyProfileBinding
import java.io.*
import java.util.*


class FacultyProfile : AppCompatActivity() {

    val APP_TAG = "crop"
    var intermediateName = "1.jpg"
    var resultName = "2.jpg"
    var intermediateProvider: Uri? = null
    var resultProvider: Uri? = null
    var galleryActivityResultLauncher: ActivityResultLauncher<Intent>? = null
    var cropActivityResultLauncher: ActivityResultLauncher<Intent>? = null
    lateinit var imageBitmap: Bitmap
    val PICK_IMAGE_REQUEST = 71

    lateinit var facultyID: String
    lateinit var collegeAcronym: String

    var imageWasChanged = false

    lateinit var firestore: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var reference: DocumentReference

    lateinit var imgLink: String

    lateinit var binding: ActivityFacultyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyProfileBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        // intent data
        val college = intent.getStringExtra("college")
        val facultyName = intent.getStringExtra("name")
        val facultyPosition = intent.getStringExtra("position")
        imgLink = intent.getStringExtra("imgLink").toString()
        facultyID = intent.getStringExtra("facultyID").toString()
        collegeAcronym = intent.getStringExtra("collegeAcronym").toString()

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        reference =
            firestore.collection(collegeAcronym).document("faculty").collection("faculty-list")
                .document(facultyID)

        binding.college.text = college
        binding.facultyName.text = facultyName
        binding.facultyPosition.text = facultyPosition

        binding.editFacultyName.setText(facultyName)
        binding.editFacultyPosition.setText(facultyPosition)

        if (imgLink != "no_image") {
            Glide.with(this).load(imgLink)
                .into(binding.image)
        } else {
            binding.image.setImageResource(R.drawable.image_placeholder)
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
                imageWasChanged = true
            }
        }
        binding.cancel.setOnClickListener {
            binding.layoutViewFaculty.visibility = View.VISIBLE
            binding.layoutEditFaculty.visibility = View.GONE
            binding.chooseImage.visibility = View.GONE
        }

        binding.saveChanges.setOnClickListener {

            val editName = binding.editFacultyName.text
            val editPosition = binding.editFacultyPosition.text

            if (editName.isEmpty() or editPosition.isEmpty()) {
                Toast.makeText(this, "Name and position can't be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            binding.progress.visibility = View.VISIBLE

            if (imageWasChanged) {
                val imageName = UUID.randomUUID().toString()


                val newImgRef = storageReference.child("images/faculty/$collegeAcronym/$imageName")

                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading Image...")
                progressDialog.show()


                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data: ByteArray = baos.toByteArray()

                val uploadTask: UploadTask = newImgRef.putBytes(data)
                uploadTask.addOnProgressListener { snapshot: UploadTask.TaskSnapshot ->
                    val progress =
                        100.0 * snapshot.bytesTransferred / snapshot.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                    newImgRef.downloadUrl
                }.addOnCompleteListener { task: Task<Uri> ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        imgLink = downloadUri.toString()
                        updateFacultyInfo()
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                }

            } else {
                updateFacultyInfo()
            }


        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_faculty_profile, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_faculty -> {
                binding.layoutViewFaculty.visibility = View.GONE
                binding.layoutEditFaculty.visibility = View.VISIBLE
                binding.chooseImage.visibility = View.VISIBLE
            }
            R.id.delete_faculty -> {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle("Confirm deletion")
                alertBuilder.setMessage("Are you sure to remove this faculty?")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                        val reference =
                            firestore.collection(collegeAcronym).document("faculty")
                                .collection("faculty-list")
                                .document(facultyID)
                        reference.delete()
                            .addOnSuccessListener {
                                onBackPressed()
                                Toast.makeText(this,
                                    "Operation successful",
                                    Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener { e: java.lang.Exception? ->
                                Toast.makeText(this,
                                    "Something wrong happened",
                                    Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("No") { dialog: DialogInterface, _: Int -> dialog.cancel() }
                alertBuilder.show()
            }
        }
        return super.onOptionsItemSelected(item)
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
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
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


    fun updateFacultyInfo() {
        val entry: MutableMap<String, Any> = HashMap()
        entry["name"] = binding.editFacultyName.text.toString()
        entry["position"] = binding.editFacultyPosition.text.toString()
        entry["img_link"] = imgLink
        reference.update(entry).addOnSuccessListener {
            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
            binding.progress.visibility = View.VISIBLE
            //finishing  Content and going directly to Entries List
            setResult(RESULT_OK, Intent())
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed updating info", Toast.LENGTH_SHORT).show()
            binding.progress.visibility = View.INVISIBLE
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