package com.example.betravel

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.betravel.databinding.ActivitySplashScreenBinding
import com.example.betravel.databinding.ActivitySplashScreenLandBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var bindingLand: ActivitySplashScreenLandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        bindingLand = ActivitySplashScreenLandBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLand.root)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        }

        Handler(Looper.getMainLooper()).postDelayed(  {
            val i = Intent(this,Login::class.java)
            startActivity(i)
            finish()
        },1000)
    }
}