package com.idn.adniwhatsappclone.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.idn.adniwhatsappclone.R
import com.idn.adniwhatsappclone.util.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.progress_layout
import kotlinx.android.synthetic.main.fragment_status_update.*

class StatusUpdateFragment : Fragment() {


    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_status_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_layout.setOnTouchListener { v, event -> true }
        fab_status.setOnClickListener { onUpdate() }
        populateImage(context, imageUrl, img_status_update)
        lay_status.setOnClickListener {
            if (isAdded) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_PHOTO)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode==REQUEST_CODE_PHOTO) {
            storeImage(data?.data)

        }
    }
    fun storeImage(imageUri: Uri?) {

        if (imageUri != null && userId != null) { // jika uri image dan userId tidak kosong
            Toast.makeText(activity, "Uploading...", Toast.LENGTH_SHORT).show()
            progress_layout.visibility = View.VISIBLE
            // menyimpang gambar status di Firebase Storage dengan nama tambahan ‘_status’
            val filePath = firebaseStorage.child(DATA_IMAGES).child("${userId}_status")
            filePath.putFile(imageUri) // menempatkan file dalam folder image di Firebase storage
                .addOnSuccessListener { // ketika berhasil menempatkan file dalam folder
                    filePath.downloadUrl // mendapatkan url file gambar yang bisa di download
                        .addOnSuccessListener { taskSnapshot ->
                            val url = taskSnapshot.toString()
                            firebaseDB.collection(DATA_USERS) // mengakses table user untuk
                                .document(userId) // memperbarui data statusUrl
                                .update(DATA_USER_STATUS, url) // sesuai data url dari file gambar
                                .addOnSuccessListener {
                                    imageUrl = url // mengisi property imageUrl dengan url gambar
                                    populateImage(context, imageUrl, img_status_update)
                                }
                            progress_layout.visibility = View.GONE
                        }
                        .addOnFailureListener { onUploadFailure() }
                }
                .addOnFailureListener { onUploadFailure() }
        }
    }

    fun onUpdate() { // ketika fab diklik akan memperbarui data-data di Firebase

        progress_layout.visibility = View.VISIBLE // sesuai acuan yang digunakan
        val map = HashMap<String, Any>()
        map[DATA_USER_STATUS] = edt_status_update.text.toString() // data status
        map[DATA_USER_STATUS_URL] = imageUrl // data statusUrl
        map[DATA_USER_STATUS_TIME] = getTime() // data statusTime

        firebaseDB.collection(DATA_USERS) // mengakses table User sesuai userId untuk
            .document(userId!!) // memperbarui data status, statusUrl,
            .update(map) // statusTime
            .addOnSuccessListener {
                progress_layout.visibility = View.GONE
                Toast.makeText(activity, "Status updated.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progress_layout.visibility = View.GONE
                Toast.makeText(activity, "Status update failed.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onUploadFailure() { // jika gagal dalam mengupload gambar setelah memilih
        Toast.makeText(activity, "Image upload failed. Please try again later",
            Toast.LENGTH_SHORT).show()
        progress_layout.visibility = View.GONE
    }
}