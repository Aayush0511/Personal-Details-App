package com.sparidtech.details

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ViewDetails : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    lateinit var ivProfileImage: ImageView
    lateinit var tvName: TextView
    lateinit var tvAge: TextView
    lateinit var tvPhone: TextView
    lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)

        supportActionBar?.title = "View Details"

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "")
        val age = sharedPreferences.getInt("age", 0)
        val phone = sharedPreferences.getString("phone", "")
        val imageBase64 = sharedPreferences.getString("image", "")

        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvName = findViewById(R.id.tvName)
        tvAge = findViewById(R.id.tvAge)
        tvPhone = findViewById(R.id.tvPhone)
        btnReset = findViewById(R.id.btnReset)

        tvName.text = name
        tvAge.text = age.toString()
        tvPhone.text = phone

        if (imageBase64 != null) {
            if (imageBase64.isNotEmpty()) {
                val b: ByteArray = Base64.decode(imageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                ivProfileImage.setImageBitmap(bitmap)
            }
        }

        btnReset.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}