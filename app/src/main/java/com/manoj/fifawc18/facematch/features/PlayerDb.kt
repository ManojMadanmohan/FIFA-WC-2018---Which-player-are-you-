package com.manoj.fifawc18.facematch.features

import android.content.Context
import com.manoj.fifawc18.facematch.models.Player
import com.manoj.fifawc18.facematch.utils.SingletonHolder
import com.manoj.fifawc18.facematch.utils.Utils
import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader

class PlayerDb private constructor(contextIn: Context) {
    private val _context: Context
    private val _playerRows: List<Array<String>>

    init {
        _context = contextIn
        val csvStream = _context.assets.open("player_list_android.csv")
        _playerRows =  CSVReader(InputStreamReader(csvStream)).readAll()
    }

    companion object: SingletonHolder<PlayerDb, Context>(:: PlayerDb) {}

    fun getPlayer(playerId: String): Player? {
        for(row in _playerRows) {
            if(row[0] == playerId) {
                val playerName = Utils.formatPlayerName(row[1])
                return Player(playerName, row[2], row[3], row[4], row[5])
            }
        }
        //no player found
        return null
    }
}
