package com.manoj.fifawc18.facematch.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import android.R.attr.data
import android.app.Activity
import android.widget.Toast
import com.manoj.fifawc18.facematch.features.ISearchFeature
import com.manoj.fifawc18.facematch.features.SearchFeature
import com.manoj.fifawc18.facematch.models.PlayerMatch
import android.provider.MediaStore
import android.graphics.Bitmap
import android.view.View
import com.google.gson.Gson
import com.manoj.fifawc18.facematch.R
import com.theartofdev.edmodo.cropper.CropImageOptions
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.main_layout.*


class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        launchCropActivity()
        loader.hide()
        retry_fm.setOnClickListener(View.OnClickListener {
            launchCropActivity()
        })
    }

    private fun launchCropActivity() {
        CropImage.activity().setScaleType(CropImageView.ScaleType.CENTER)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri.toString()
                makeRequest(resultUri)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun makeRequest(resultUri: String) {
        retry_fm.visibility = View.GONE
        loader.show();
        SearchFeature.getInstance(this).findMatchingPlayer(resultUri.toString(), object : ISearchFeature.CompletionListener {
            override fun onComplete(matchingPlayer: PlayerMatch) {
                onResultObtained(matchingPlayer, resultUri)
            }
        })
    }

    private fun onResultObtained(matchingPlayer: PlayerMatch, resultUri: String) {
        retry_fm.visibility = View.VISIBLE
        loader.hide()
        val gson = Gson()
        val playerMatchString = gson.toJson(matchingPlayer)
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        intent.putExtra(PLAYER_MATCH_EXTRA, playerMatchString)
        intent.putExtra(SELF_IMAGE_EXTRA, resultUri)
        startActivity(intent)
    }
}
