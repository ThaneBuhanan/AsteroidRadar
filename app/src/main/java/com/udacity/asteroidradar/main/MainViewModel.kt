package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {
    private val _asteroids = MutableLiveData<MutableList<Asteroid>>()
    val asteroids: LiveData<MutableList<Asteroid>>
        get() = _asteroids
}