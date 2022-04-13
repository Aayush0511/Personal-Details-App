package com.sparidtech.details

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    lateinit var ivProfileImage: ImageView
    lateinit var etName: EditText
    lateinit var etAge: EditText
    lateinit var etPhone: EditText
    lateinit var btnSubmit: Button

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Enter Details"

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "")
        if(name!!.isNotEmpty()) {
            val intent = Intent(this, ViewDetails::class.java)
            startActivity(intent)
            finish()
        }

        ivProfileImage = findViewById(R.id.ivProfileImage)
        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etPhone = findViewById(R.id.etPhone)
        btnSubmit = findViewById(R.id.btnSubmit)

        ivProfileImage.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            } else {
                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, 101)
            }
        }

        btnSubmit.setOnClickListener {
            if ("Vector" in ivProfileImage.drawable.toString()) {
                Toast.makeText(this, "Please click a profile image", Toast.LENGTH_LONG).show()
            } else if (etName.text.isEmpty()) {
                Toast.makeText(this, "Name cannot be left blank", Toast.LENGTH_LONG).show()
            } else if (etAge.text.isEmpty()) {
                Toast.makeText(this, "Age cannot be left blank", Toast.LENGTH_LONG).show()
            } else if (etPhone.text.isEmpty()) {
                Toast.makeText(this, "Phone number cannot be left blank", Toast.LENGTH_LONG).show()
            } else {
                sharedPreferences.edit().putString("name", etName.text.toString()).apply()                          // .apply() is asynchronous and does not return any value
                sharedPreferences.edit().putInt("age", Integer.parseInt(etAge.text.toString())).apply()             // .commit() is synchronous (blocks the current thread) and returns a boolean value whether the operation was a success or not
                sharedPreferences.edit().putString("phone", etPhone.text.toString()).apply()

                val intent = Intent(this@MainActivity, ViewDetails::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101) {
            val pic = data?.getParcelableExtra<Bitmap>("data")
            if (pic != null) {
                ivProfileImage.setImageBitmap(pic)

                val baos = ByteArrayOutputStream()
                pic.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b: ByteArray = baos.toByteArray()
                val encodedImage: String = Base64.encodeToString(b, Base64.DEFAULT)
                sharedPreferences.edit().putString("image", encodedImage).apply()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)
        }
    }
}
