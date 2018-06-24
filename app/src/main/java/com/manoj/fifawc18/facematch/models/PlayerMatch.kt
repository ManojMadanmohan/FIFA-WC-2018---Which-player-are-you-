package com.manoj.fifawc18.facematch.models

data class PlayerMatch(val player: Player?, val matchScore: Float) {

    fun isMatched(): Boolean {
        if(matchScore > 0 && player!= null) {
            return true
        } else {
            return false
        }
    }
}