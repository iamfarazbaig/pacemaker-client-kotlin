package models

import java.util.UUID

/**
 * Message class
 */
data class Message(
	var message: String = "",
	var from: String = "",
	val id: String = UUID.randomUUID().toString()
)