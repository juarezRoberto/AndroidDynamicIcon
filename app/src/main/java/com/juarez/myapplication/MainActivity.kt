package com.juarez.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.juarez.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences("fileName", Context.MODE_PRIVATE)
        val db = Firebase.firestore

        db.collection("appIcon").get()
            .addOnSuccessListener { docs ->

                val iconSaved = getIconPreferences()
                val icons = docs.toObjects<IconApp>()
                icons.map {
                    if (it.enabled) {
                        if (iconSaved.isNullOrEmpty()) {
                            Log.d("TAG", "icon saved is null or empty")
                            setIconPreferences(it.name)
                            AppIconUtils.setNewIcon(this, it.name)
                        } else {
                            Log.d("TAG", "icon already saved: $iconSaved")
                            if (iconSaved != it.name) {
                                setIconPreferences(it.name)
                                AppIconUtils.setNewIcon(this, it.name)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents", exception)
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

    private fun setIconPreferences(icon: String) =
        with(sharedPref.edit()) {
            putString("iconApp", icon)
            apply()
        }

    private fun getIconPreferences(): String? = sharedPref.getString("iconApp", "")

    override fun onDestroy() {
        super.onDestroy()
        //setNewIcon()
    }
}