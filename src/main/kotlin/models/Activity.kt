package models

import java.util.UUID

/**
 * this is the activity class which initializes some variables.
 */
data class Activity(
	val type: String = "",
	val location: String = "",
	val distance: Float = 0.0f,
	val id: String = UUID.randomUUID().toString(),
	val route: MutableList<Location> = ArrayList()
)   
