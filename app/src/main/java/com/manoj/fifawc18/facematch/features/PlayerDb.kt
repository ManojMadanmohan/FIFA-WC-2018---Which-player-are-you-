package com.manoj.fifawc18.facematch.features

import android.content.Context
import com.manoj.fifawc18.facematch.models.Player
import com.manoj.fifawc18.facematch.utils.SingletonHolder
import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader

class PlayerDb private constructor(contextIn: Context) {
    private val _context: Context
    private val _playerRows: List<Array<String>>

    init {
        _context = contextIn
        val csvfileString = _context.getApplicationInfo().dataDir + File.separatorChar + "player_list_android.csv"
        val csvFile = File(csvfileString);
        _playerRows =  CSVReader(FileReader(csvFile)).readAll()
    }

    companion object: SingletonHolder<PlayerDb, Context>(:: PlayerDb) {}

    fun getPlayer(playerId: String): Player? {
        for(row in _playerRows) {
            if(row[0] == playerId) {
                return Player(row[1], row[2], row[3], row[4], row[5])
            }
        }
        //no player found
        return null
    }
}
