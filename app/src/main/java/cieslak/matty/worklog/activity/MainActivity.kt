package cieslak.matty.worklog.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cieslak.matty.worklog.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navigateLogWork(view: View) {
        val intent = Intent(this, LogWorkActivity::class.java)
        startActivity(intent)
    }
}