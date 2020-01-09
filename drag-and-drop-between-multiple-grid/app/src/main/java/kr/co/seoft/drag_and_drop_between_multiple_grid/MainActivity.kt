package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        DadigActivity.startActivity(this,3)
        
    }


}
