package com.manoj.fifawc18.facematch.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import android.app.Activity
import android.widget.Toast
import com.manoj.fifawc18.facematch.features.ISearchFeature
import com.manoj.fifawc18.facematch.features.SearchFeature
import com.manoj.fifawc18.facematch.models.PlayerMatch
import android.view.View
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.manoj.fifawc18.facematch.R
import com.manoj.fifawc18.facematch.utils.Utils
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.main_layout.*


class MainActivity: AppCompatActivity() {

    private val ERROR_CROP_COPY = "Could not recieve image. Please check permissions and try again"
    private val ERROR_AWS_COPY = "Something's gone wrong - It's likely that no face was detected in the image. Please try again with another image"
    private val ERROR_NETWORK_COPY = "Network issue - please check your connection and try again"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        showDefaultState()
        main_action.setOnClickListener(View.OnClickListener {
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
                if(Utils.isNetworkAvailable(this)) {
                    val resultUri = result.uri.toString()
                    makeRequest(resultUri)
                } else {
                    showErrorToast(ERROR_NETWORK_COPY)
                }
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Crashlytics.logException(error)
                showErrorToast(ERROR_CROP_COPY)
            }
        }
    }

    private fun makeRequest(resultUri: String) {
        showLoadingState()
        SearchFeature.getInstance(this).findMatchingPlayer(resultUri.toString(), object : ISearchFeature.CompletionListener {
            override fun onComplete(matchingPlayer: PlayerMatch) {
                showDefaultState()
                launchResultsScreen(matchingPlayer, resultUri)
            }

            override fun onError(throwable: Throwable) {
                Crashlytics.logException(throwable)
                showDefaultState()
                showErrorToast(ERROR_AWS_COPY)
            }
        })
    }

    private fun launchResultsScreen(matchingPlayer: PlayerMatch, resultUri: String) {
        val gson = Gson()
        val playerMatchString = gson.toJson(matchingPlayer)
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        intent.putExtra(PLAYER_MATCH_EXTRA, playerMatchString)
        intent.putExtra(SELF_IMAGE_EXTRA, resultUri)
        startActivity(intent)
    }

    private fun showDefaultState() {
        main_action.isEnabled = true
        main_frame.alpha = 1.0f
        loader.hide()
    }

    private fun showLoadingState() {
        loader.show();
        main_action.isEnabled = false
        main_frame.alpha = 0.4f
    }

    private fun showErrorToast(errorCopy: String) {
        Toast.makeText(this, errorCopy, Toast.LENGTH_LONG).show()
    }
}
