package com.manoj.fifawc18.facematch.config

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.manoj.fifawc18.facematch.utils.SingletonHolder

class AppConfig private constructor(contextIn: Context) {

    private val _context = contextIn

    companion object: SingletonHolder<AppConfig, Context> (::AppConfig) {

    }

    fun getAwsCredentials(): CognitoCachingCredentialsProvider {
        return AWSConfig.getInstance(_context).getCredentialsProvider()
    }
}
