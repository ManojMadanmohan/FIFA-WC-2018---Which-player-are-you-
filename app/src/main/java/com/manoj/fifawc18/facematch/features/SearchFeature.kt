package com.manoj.fifawc18.facematch.features

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.amazonaws.services.rekognition.model.FaceMatch
import com.manoj.fifawc18.facematch.aws.AWSWrapper
import com.manoj.fifawc18.facematch.models.Player
import com.manoj.fifawc18.facematch.models.PlayerMatch
import com.manoj.fifawc18.facematch.utils.SingletonHolder

class SearchFeature private constructor(contextIn: Context): ISearchFeature {

    private val context: Context = contextIn
    private val awsClient: AWSWrapper
    private val AWS_FACE_COLLECTION_ID: String = "fifa_wc2018_players"

    init {
        awsClient = AWSWrapper.getInstance(context)
    }

    companion object: SingletonHolder<SearchFeature, Context>(::SearchFeature) {

    }

    override fun findMatchingPlayer(resultUri: String, listener: ISearchFeature.CompletionListener) {
        var thread = HandlerThread("background-thread")
        thread.start()
        var backgroundHandler = Handler(thread.looper)
        var runnable = Runnable {
            try{
                var playerMatch = findMatchingPlayerSync(resultUri)
                postOnMainThread(Runnable {
                    listener.onComplete(playerMatch)
                })
            } catch (exception: Exception) {
                postOnMainThread(Runnable {
                    listener.onError(exception)
                })
            }
        }
        backgroundHandler.post(runnable)
    }

    private fun findMatchingPlayerSync(resultUri: String): PlayerMatch {
        val matches = awsClient.searchForMatchingFace(resultUri, AWS_FACE_COLLECTION_ID)
        var bestMatch: FaceMatch? = null
        var bestScore = 0.0f
        for(match in matches)
        {
            if(match.similarity > bestScore)
            {
                bestMatch = match
                bestScore = match.similarity
            }
        }

        if(bestMatch != null) {
            val playerId = bestMatch.face.externalImageId

            val player = PlayerDb.getInstance(context).getPlayer(playerId)
            return PlayerMatch(player, bestScore)
        } else {
            return PlayerMatch(null, 0.0f)
        }

    }

    private fun postResultOnMainThread(playerMatch: PlayerMatch, listener: ISearchFeature.CompletionListener) {

    }

    private fun postOnMainThread(runnable: Runnable) {
        var mainHandler = Handler(context.mainLooper)
        mainHandler.post(runnable)
    }
}
