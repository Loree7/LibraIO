package com.lorenzoprogramma.libraio.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animation = AnimationUtils.loadAnimation(this, R.anim.move_up_and_fade_out)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                setResult(Activity.RESULT_OK, intent)
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        binding.splashScreenLogo.startAnimation(animation)

    }
}