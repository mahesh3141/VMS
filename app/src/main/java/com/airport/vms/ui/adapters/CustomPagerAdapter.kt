package com.airport.vms.ui.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.airport.vms.R


class CustomPagerAdapter(var context: Context,var pager:ArrayList<Int>) : PagerAdapter() {


    override fun getCount(): Int {
        return pager.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.row_viewpager, container, false)
        val imageView: ImageView = view.findViewById<View>(R.id.image) as ImageView
        imageView.setBackgroundResource(pager[position])
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, data: Any): Boolean {
       return view == data
    }


  override  fun destroyItem(container: View, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View?
        vp.removeView(view)
    }

    override fun getItemPosition(data: Any): Int {
        return super.getItemPosition(data)
    }
}