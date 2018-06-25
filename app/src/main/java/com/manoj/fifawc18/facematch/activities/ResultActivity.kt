package com.manoj.fifawc18.facematch.activities

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.manoj.fifawc18.facematch.R
import com.manoj.fifawc18.facematch.models.PlayerMatch
import org.w3c.dom.Text
import kotlinx.android.synthetic.main.result_layout.*
import java.net.URL

const val SELF_IMAGE_EXTRA: String = "SELF_IMAGE_EXTRA"
const val PLAYER_MATCH_EXTRA: String = "PLAYER_IMAGE_EXTRA"

class ResultActivity: AppCompatActivity() {

    private var selfImageUri: String? = null
    private var playerMatch: PlayerMatch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_layout)
        extractData()
        initView()
    }

    fun extractData() {
        selfImageUri = intent.getStringExtra(SELF_IMAGE_EXTRA)
        val playerMatchStr = intent.getStringExtra(PLAYER_MATCH_EXTRA)
        playerMatch = Gson().fromJson(playerMatchStr, PlayerMatch::class.java)
    }

    fun initView() {
        val player = playerMatch!!.player!!
        Glide.with(this).load(Uri.parse(selfImageUri)).into(self_image)
        Glide.with(this).load(Uri.parse(player.imageUrl)).into(player_image)
        match_score.setText(playerMatch!!.matchScore.toInt().toString()+"% match")
        player_name.setText(player.name)
        val playerInfoStr = "No "+player.jerseyNum+", "+player.country
        player_info.setText(playerInfoStr)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.result_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null) {
            if(item.itemId == R.id.result_menu_retry) {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}