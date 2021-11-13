package kr.co.seoft.antonio_sample.ui

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.antonio_sample.R
import kr.co.seoft.antonio_sample.ui.antonio.AntonioActivity
import kr.co.seoft.antonio_sample.ui.normal.NormalActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    fun btNormalRecyclerView(view: View) {
        startActivity(Intent(this, NormalActivity::class.java))
    }

    fun btAntonioRecyclerView(view: View) {
        startActivity(Intent(this, AntonioActivity::class.java))
    }

}