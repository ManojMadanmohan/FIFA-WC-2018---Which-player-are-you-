package com.manoj.fifawc18.facematch.aws

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.FaceMatch
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult
import com.amazonaws.util.IOUtils
import com.manoj.fifawc18.facematch.config.AppConfig
import com.manoj.fifawc18.facematch.utils.SingletonHolder
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.InputStream


class AWSWrapper private constructor(contextIn: Context): IAWSWrapper {

    private val context: Context = contextIn

    private val awsRekognitionClient: AmazonRekognitionClient

    init{
        val awsCredentials = AppConfig.getInstance(context).getAwsCredentials()
        awsRekognitionClient = AmazonRekognitionClient(awsCredentials)
    }

    companion object: SingletonHolder<AWSWrapper, Context>(:: AWSWrapper) {}

    override fun searchForMatchingFace(resultUri: String, collectionId: String): List<FaceMatch> {
        val request = getSearchFacesRequest(resultUri, collectionId)
        val result = awsRekognitionClient.searchFacesByImage(request)
        return result.faceMatches
    }

    private fun getSearchFacesRequest(resultUri: String, collectionId: String): SearchFacesByImageRequest {
        val byteBuffer = getByteBuffer(resultUri)
        val image = Image().withBytes(byteBuffer)
        val request = SearchFacesByImageRequest(collectionId, image).withFaceMatchThreshold(0.0f)
        return request
    }

    private fun getByteBuffer(resultUri: String): ByteBuffer {
        val inputStream = context.contentResolver.openInputStream(Uri.parse(resultUri));
        val imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream))
        return imageBytes
    }

}
