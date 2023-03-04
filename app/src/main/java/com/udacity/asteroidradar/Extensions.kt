package com.udacity.asteroidradar

import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.model.Asteroid

fun Asteroid.asDatabaseModel() = DatabaseAsteroid(
    id = id,
    codename = codename,
    closeApproachDate = closeApproachDate,
    absoluteMagnitude = absoluteMagnitude,
    estimatedDiameter = estimatedDiameter,
    relativeVelocity = relativeVelocity,
    distanceFromEarth = distanceFromEarth,
    isPotentiallyHazardous = isPotentiallyHazardous,
)

fun DatabaseAsteroid.asDomainModel() = Asteroid(
    id = id,
    codename = codename,
    closeApproachDate = closeApproachDate,
    absoluteMagnitude = absoluteMagnitude,
    estimatedDiameter = estimatedDiameter,
    relativeVelocity = relativeVelocity,
    distanceFromEarth = distanceFromEarth,
    isPotentiallyHazardous = isPotentiallyHazardous,
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map { it.asDomainModel() }
}

fun List<Asteroid>.asDatabaseModel(): List<DatabaseAsteroid> {
    return map { it.asDatabaseModel() }
}