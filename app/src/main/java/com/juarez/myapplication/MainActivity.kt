package com.juarez.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.juarez.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var firebaseIconName: String
    private var iconSaved: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences("fileName", Context.MODE_PRIVATE)
        val db = Firebase.firestore
        iconSaved = getIconPreferences()

        db.collection("appIcon").get()
            .addOnSuccessListener { docs ->
                val icons = docs.toObjects<IconApp>()
                icons.map {
                    if (it.enabled) {
                        firebaseIconName = it.name
//                        checkAppIcon()
                    }
                }
            }
            .addOnFailureListener { ex ->
                Toast.makeText(
                    this,
                    "Error getting documents ${ex.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        binding.btnSetIconApp.setOnClickListener {
            val iconName = binding.edtIconName.text.toString()
            if (iconName.isNotEmpty()) {
                AppIconUtils.setNewIcon(this, iconName)
                setIconPreferences(iconName)
            }
        }

        binding.btnReset.setOnClickListener {
            AppIconUtils.reset(this)
        }
    }

    private fun checkAppIcon() {
        if (iconSaved.isNullOrEmpty()) {
            Log.d("TAG", "icon is not saved")
            setIconPreferences(firebaseIconName)
            AppIconUtils.setNewIcon(this, firebaseIconName)
        } else {
            Log.d("TAG", "icon already saved: $iconSaved")
            if (iconSaved != firebaseIconName) {
                setIconPreferences(firebaseIconName)
                AppIconUtils.setNewIcon(this, firebaseIconName)
            }
        }
    }

    private fun setIconPreferences(icon: String) =
        with(sharedPref.edit()) {
            putString("iconApp", icon)
            apply()
        }

    private fun getIconPreferences(): String? = sharedPref.getString("iconApp", "")

    override fun onDestroy() {
        super.onDestroy()
        checkAppIcon()
    }
}