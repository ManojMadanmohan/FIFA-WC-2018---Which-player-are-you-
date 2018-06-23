package com.manoj.fifawc18.facematch.config

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.manoj.fifawc18.facematch.utils.SingletonHolder

class AWSConfig private constructor(contextIn: Context){

    private val awsIdentityPoolId = ""
    private val _context = contextIn

    init {

    }
    companion object: SingletonHolder<AWSConfig, Context> (::AWSConfig) {

    }

    fun getCredentialsProvider(): CognitoCachingCredentialsProvider {
        return CognitoCachingCredentialsProvider(_context,
                awsIdentityPoolId,// Identity pool ID
                Regions.US_EAST_1 // Region
        )
    }

}
