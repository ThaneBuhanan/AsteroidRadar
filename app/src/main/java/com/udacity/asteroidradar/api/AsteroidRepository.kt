package com.udacity.asteroidradar.api


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.switchMap
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.asDomainModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.main.AsteroidFilter
import com.udacity.asteroidradar.model.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class AsteroidRepository(private val database: AsteroidsDatabase) {
    private val _filterType: MutableLiveData<AsteroidFilter> = MutableLiveData()

    private val _asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    val asteroids: LiveData<List<Asteroid>> = _filterType.switchMap { filter ->
        when (filter) {
            AsteroidFilter.WEEK -> _asteroids
            AsteroidFilter.ALL_SAVED -> _asteroids
            AsteroidFilter.TODAY -> {
                val todaysAsteroids = _asteroids.value!!.filter {
                    it.closeApproachDate == getTodaysDate()
                }
                MutableLiveData(todaysAsteroids)
            }
            else -> _asteroids
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTodaysDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return sdf.format(calendar.time)
    }

    suspend fun saveAsteroids(apiKey: String) {
        withContext(Dispatchers.IO) {
            val jsonString = Network.asteroidService.getAsteroids(apiKey)
            val jsonObject = JSONObject(jsonString)
            val asteroids = parseAsteroidsJsonResult(jsonObject)
            val databaseAsteroids = asteroids.asDatabaseModel().toTypedArray()
            database.clearAllTables()
            database.asteroidDao.insertAll(*databaseAsteroids)
        }
    }

    fun setFilter(asteroidFilter: AsteroidFilter) {
        _filterType.value = asteroidFilter
    }
}
