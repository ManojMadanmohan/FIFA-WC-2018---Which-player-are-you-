package com.manoj.fifawc18.facematch.aws

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.FaceMatch
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest
import com.amazonaws.util.IOUtils
import com.manoj.fifawc18.facematch.config.AppConfig
import com.manoj.fifawc18.facematch.utils.SingletonHolder
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap.CompressFormat
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions


class AWSWrapper private constructor(contextIn: Context): IAWSWrapper {

    private val context: Context = contextIn

    private val awsRekognitionClient: AmazonRekognitionClient

    init{
        val awsCredentials = AppConfig.getInstance(context).getAwsCredentials()
        awsRekognitionClient = AmazonRekognitionClient(awsCredentials)
        awsRekognitionClient.setRegion(Region.getRegion(Regions.US_WEST_2))
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
        val origBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(resultUri))
        val scaledBitmap = Bitmap.createScaledBitmap(origBitmap, 300, 300, true);

        val bos = ByteArrayOutputStream()
        scaledBitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()
        val bs = ByteArrayInputStream(bitmapdata)

        val imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(bs))
        return imageBytes
    }

}
