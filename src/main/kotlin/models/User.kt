package models

import java.util.UUID

/**
 * User class
 */
data class User(
	val firstname: String = "",
	val lastname: String = "",
	val email: String = "",
	val password: String = "",
	val friend: MutableList<String> = ArrayList(),
	val id: String = UUID.randomUUID().toString(),
	val disabled: Boolean = false,
	val admin: Boolean = false
)