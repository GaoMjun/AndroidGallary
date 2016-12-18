package io.github.gaomjun.gallary.gallary_slider.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v4.view.ViewPager
import android.view.Menu
import android.widget.ShareActionProvider
import io.github.gaomjun.gallary.R
import io.github.gaomjun.gallary.gallary_slider.adapter.GallarySliderViewPagerAdapter
import io.github.gaomjun.gallary.gallary_slider.model.GallarySliderData
import io.github.gaomjun.gallary.gallary_slider.model.GallarySliderItem
import kotlinx.android.synthetic.main.activity_gallary_slider.*
import java.io.File
import java.util.*


/**
 * Created by qq on 13/12/2016.
 */

class GallarySliderActivity : Activity(), ViewPager.OnPageChangeListener {

    private var mShareActionProvider: ShareActionProvider? = null

    private var gallarySliderData: GallarySliderData? = null
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallary_slider)

        currentPosition = intent.getIntExtra("position", 0)
        val pathArray = intent.getStringArrayExtra("pathArray")
        val typeArray = intent.getBooleanArrayExtra("typeArray")

        initGallarySliderData(pathArray, typeArray)
        setupGallarySliderViewPager()
    }

    private fun initGallarySliderData(pathArray: Array<out String>, typeArray: BooleanArray) {

        var data: MutableList<GallarySliderItem> = ArrayList()

        for (i in pathArray?.indices!!) {
            data.add(GallarySliderItem(pathArray[i], typeArray[i]))
        }

        gallarySliderData = GallarySliderData(data)
    }

    private fun setupGallarySliderViewPager() {
        val gallarySliderViewPagerAdapter = GallarySliderViewPagerAdapter(this, gallarySliderData!!.data)
        gallarySliderViewPager.adapter = gallarySliderViewPagerAdapter
        gallarySliderViewPager.offscreenPageLimit = 2
        gallarySliderViewPager.currentItem = currentPosition
        gallarySliderViewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        prepareShare(position)
    }

    private fun prepareShare(position: Int) {
        val item = gallarySliderData?.data?.get(position)

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/*"
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(item?.path)))

        setShareIntent(share)
        println("prepare share")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate menu resource file.
        menuInflater.inflate(R.menu.share_menu, menu)

        // Locate MenuItem with ShareActionProvider
        val item = menu?.findItem(R.id.menu_item_share)

        // Fetch and store ShareActionProvider
        mShareActionProvider = item?.actionProvider as ShareActionProvider

        prepareShare(currentPosition)
        
        // Return true to display menu
        return true
    }

    // Call to update the share intent
    private fun setShareIntent(shareIntent: Intent) {
        if (mShareActionProvider != null) {
            mShareActionProvider?.setShareIntent(shareIntent)
        }
    }
}

