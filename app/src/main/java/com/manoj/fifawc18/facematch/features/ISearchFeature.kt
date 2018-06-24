package com.manoj.fifawc18.facematch.features

import android.graphics.Bitmap
import com.manoj.fifawc18.facematch.models.PlayerMatch

interface ISearchFeature {
    interface CompletionListener{
        /*
        Returns the matching player with maximum score (along with the player and the score).
        returns a player match with null player and 0 score in case no match found
         */
        fun onComplete(matchingPlayer: PlayerMatch)
    }

    fun findMatchingPlayer(resultUri: String, listener: CompletionListener)
}