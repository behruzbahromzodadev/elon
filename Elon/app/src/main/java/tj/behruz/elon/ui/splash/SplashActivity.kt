package tj.behruz.elon.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import tj.behruz.elon.MainActivity
import tj.behruz.elon.R
import tj.behruz.elon.databinding.SplashScreenBinding


class SplashActivity : AppCompatActivity() {


    private val SPLASH_TIME_OUT: Long=3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: SplashScreenBinding = DataBindingUtil.setContentView(
            this,
            R.layout.splash_screen
        )
        Handler().postDelayed(Runnable { // This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)


    }


}