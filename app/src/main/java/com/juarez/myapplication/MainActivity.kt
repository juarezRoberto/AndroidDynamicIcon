package com.juarez.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.juarez.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFisrtIcon.setOnClickListener {
            AppIconUtils.setNewIcon(this@MainActivity, "FirstIconAlias")
        }

        binding.btnSecondIcon.setOnClickListener {
            AppIconUtils.setNewIcon(this@MainActivity, "SecondIconAlias")
        }
        binding.btnReset.setOnClickListener {
            reset()
        }
    }

    private fun reset() {
        AppIconUtils.reset(this@MainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        //setNewIcon()
    }
}