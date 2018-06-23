package com.manoj.fifawc18.facematch.aws

import android.content.Context
import android.graphics.Bitmap
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.FaceMatch
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult
import com.manoj.fifawc18.facematch.config.AppConfig
import com.manoj.fifawc18.facematch.utils.SingletonHolder
import java.nio.ByteBuffer

class AWSWrapper private constructor(contextIn: Context): IAWSWrapper {

    private val context: Context = contextIn

    private val awsRekognitionClient: AmazonRekognitionClient

    init{
        val awsCredentials = AppConfig.getInstance(context).getAwsCredentials()
        awsRekognitionClient = AmazonRekognitionClient(awsCredentials)
    }

    companion object: SingletonHolder<AWSWrapper, Context>(:: AWSWrapper) {}

    override fun searchForMatchingFace(bitmap: Bitmap, collectionId: String): List<FaceMatch> {
        val request = getSearchFacesRequest(bitmap, collectionId)
        val result = awsRekognitionClient.searchFacesByImage(request)
        return result.faceMatches
    }

    private fun getSearchFacesRequest(bitmap: Bitmap, collectionId: String): SearchFacesByImageRequest {
        val byteBuffer = getByteBuffer(bitmap)
        val image = Image().withBytes(byteBuffer)
        val request = SearchFacesByImageRequest(collectionId, image)
        return request
    }

    private fun getByteBuffer(bitmap: Bitmap): ByteBuffer {
        val bytes = bitmap.byteCount
        val byteBuffer = ByteBuffer.allocate(bytes)

        bitmap.copyPixelsToBuffer(byteBuffer)

        return byteBuffer;
    }

}
