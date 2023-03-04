package com.udacity.asteroidradar.api


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.asDomainModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    //apikey needed below
    suspend fun saveAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonString = Network.asteroidService
                .getAsteroids(BuildConfig.API_KEY)
            val jsonObject = JSONObject(jsonString)
            val asteroids = parseAsteroidsJsonResult(jsonObject)
            val databaseAsteroids = asteroids.asDatabaseModel().toTypedArray()
            database.clearAllTables()
            database.asteroidDao.insertAll(*databaseAsteroids)
        }
    }
}
