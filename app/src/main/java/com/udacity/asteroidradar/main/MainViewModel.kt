package com.udacity.asteroidradar.main


import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.AsteroidRepository
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.launch

enum class AsteroidFilter {
    TODAY, ALL_SAVED, WEEK
}

class MainViewModel(
    application: Application
) : ViewModel() {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    val asteroids = asteroidRepository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        viewModelScope.launch {
            try {
                asteroidRepository.saveAsteroids(BuildConfig.API_KEY)
                asteroidRepository.setFilter(AsteroidFilter.ALL_SAVED)
                _pictureOfDay.value =
                    Network.pictureOfDayService.getPicutureOfDay(BuildConfig.API_KEY)
            } catch (ex: Exception) {

            }
        }
    }

    fun setFilter(asteroidFilter: AsteroidFilter) {
        asteroidRepository.setFilter(asteroidFilter)
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}