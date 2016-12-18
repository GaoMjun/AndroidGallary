package io.github.gaomjun.androidgallary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

import io.github.gaomjun.gallary.gallary_grid.ui.GallaryGridActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openGallaryButton.setOnClickListener {
            startActivity(Intent(this, GallaryGridActivity::class.java))
        }
    }
}

