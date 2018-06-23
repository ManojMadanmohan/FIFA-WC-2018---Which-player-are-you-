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


class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CropImage.activity().start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
            SearchFeature.getInstance(this).findMatchingPlayer(result.bitmap, object: ISearchFeature.CompletionListener {
                override fun onComplete(matchingPlayer: PlayerMatch) {
                    var name = matchingPlayer.player
                    var score = matchingPlayer.matchScore
                    Toast.makeText(this@MainActivity, "name = "+name+" score = "+score, Toast.LENGTH_LONG ).show();
                }
            })
        }
    }
}
