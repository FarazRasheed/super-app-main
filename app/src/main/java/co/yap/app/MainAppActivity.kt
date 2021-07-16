package co.yap.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yap.yapandroid.MainYapActivity
import com.yap.yapandroid.main.MainActivity

class MainAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sample)

        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onclick(view: View){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}