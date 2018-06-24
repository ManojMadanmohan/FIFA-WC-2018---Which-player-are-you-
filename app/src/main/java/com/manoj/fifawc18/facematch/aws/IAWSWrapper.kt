package com.manoj.fifawc18.facematch.aws

import android.content.Context
import android.graphics.Bitmap
import com.amazonaws.services.rekognition.model.FaceMatch
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult

interface IAWSWrapper {
    /*
    Blocking call which needs to be made on background thread
     */
    fun searchForMatchingFace(resultUri: String, collectionId: String) : List<FaceMatch>
}