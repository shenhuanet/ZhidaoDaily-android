package com.shenhua.zhidaodaily.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.shenhua.zhidaodaily.R
import com.shenhua.zhidaodaily.core.ContentRepository
import com.shenhua.zhidaodaily.utils.Utils
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.fragment_photo.view.*


/**
 * Created by shenhua on 2018/10/22.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class PhotoFragment : Fragment() {

    private val contentRegistry = ContentRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Utils.liteStatusBarIcon(activity?.window)
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        val image = arguments?.getString("image")
        Glide.with(view).load(image).transition(DrawableTransitionOptions.withCrossFade()).into(view.photoView)
        view.photoView.tag = image
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoView.setOnPhotoTapListener { _, _, _ -> NavHostFragment.findNavController(this).navigateUp() }
        save.setOnClickListener { checkPermission() }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0x501)
        } else {
            contentRegistry.saveImage(photoView)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0x501) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contentRegistry.saveImage(photoView)
            } else {
                Snackbar.make(photoView, "请授予存储访问权限才能保存图片", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}