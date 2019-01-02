package controllers

import asg.cliche.*
import parsers.AsciiTable

class PacemakerConsoleService(apiUrl: String) : ShellDependent {
	private val paceApi = PacemakerAPI(apiUrl)
	private val console = AsciiTable()
	private lateinit var myShell: Shell

	override fun cliSetShell(myShell: Shell) {
		this.myShell = myShell
	}

	private object UserConsoleService {
		var id: String? = null
		fun loggedIn(): Boolean {
			return id != null
		}
	}

	@Command(name="login-user", abbrev="lu", description = "Login: Log in a registered user")
	fun login(
		@Param(name = "email") email: String,
		@Param(name = "password") password: String
	) {
		val user = paceApi.getUserByEmail(email)
		if (user != null) {
			if (user.password.equals(password) && !user.disabled) {
				UserConsoleService.id = user.id
				ShellFactory.createSubshell(
					user.email,
					myShell,
					"Welcome " + user.firstname + ", ?list for commands, type 'exit' to logout",
					UserConsole()
				).commandLoop()
			} else {
				console.println("Wrong email/password")
			}
		} else {
			console.println("No such user found")
		}
	}

	@Command(name="admin-login", abbrev="alu", description = "Admin: Log in as an admin")
	fun admin(
		@Param(name = "email") email: String,
		@Param(name = "password") password: String
	) {
		val user = paceApi.getUserByEmail(email)
		if (user != null) {
			if (user.password.equals(password) && user.admin && !user.disabled) {
				UserConsoleService.id = user.id
				ShellFactory.createSubshell(
					user.email,
					myShell,
					"Welcome Administrator " + user.firstname + ", ?list for commands, type 'exit' to logout",
					AdminConsole()
				).commandLoop()
			} else {
				console.println("Bad credentials")
			}
		} else {
			console.println("Invalid user")
		}
	}

	/**
	 *  // The commands for logged in admin.
	 */
	inner class AdminConsole {

		@Command(name="exit", abbrev="exit", description = "Logout current user")
		fun exit() {
			println("Logged out ")
			UserConsoleService.id = null
		}

		@Command(name="get-users", abbrev="gu", description = "To get all users emails, first and last names")
		fun listUsers() {
			console.renderUsers(paceApi.getUsers())
		}

		@Command(name="delete-users", abbrev="delu", description = "To Delete a user ")
		fun DeleteUser(@Param(name = "id") id: String) {
			if (paceApi.deleteUser(id)) {
				console.println("$id user deleted")
			} else {
				console.println("user not found")
			}
		}

		@Command(name="disable-login", abbrev="dl", description = "Disable login for a user ")
		fun blockUser(@Param(name = "id") id: String) {
			var user = paceApi.getUser(id)
			if (user != null) {
				if (paceApi.updateUser(
						id,
						user.firstname,
						user.lastname,
						user.email,
						user.password,
						true,
						user.admin
					) != null
				) {
					console.println("$id user is disabled")
				} else {
					console.println("unable to update $id user status")
				}
			} else {
				console.println("user not found")
			}
		}

		@Command(name="enable-login", abbrev="el", description = "enable login for a user ")
		fun unblockUser(@Param(name = "id") id: String) {
			var user = paceApi.getUser(id)
			if (user != null) {
				if (paceApi.updateUser(
						id,
						user.firstname,
						user.lastname,
						user.email,
						user.password,
						false,
						user.admin
					) != null
				) {
					console.println("enabled user login")
				} else {
					console.println("Failed to update $id user")
				}
			} else {
				console.println("User not found")
			}
		}

		@Command(name="set-password", abbrev="sp", description = "Set the password for a user")
		fun setPass(@Param(name = "id") id: String, @Param(name = "password") password: String) {
			var user = paceApi.getUser(id)
			if (user != null) {
				if (paceApi.updateUser(
						id,
						user.firstname,
						user.lastname,
						user.email,
						password,
						user.disabled,
						user.admin
					) != null
				) {
					console.println("Success!")
				} else {
					console.println("could not update $id id of user")
				}
			} else {
				console.println("user not found")
			}
		}

		@Command(name="register-user", abbrev="ru", description = "Registering an account for a new user")
		fun registerUser(
			@Param(name = "first name") firstName: String,
			@Param(name = "last name") lastName: String, @Param(name = "email") email: String,
			@Param(name = "password") password: String
		) {
			if (paceApi.createUser(firstName, lastName, email, password) != null) {
				console.println("added user")
			} else {
				console.println("Error!")
			}

		}
	}

	/**
	 * commands for logged in user.
	 */
	inner class UserConsole {

		@Command(name="exit", abbrev="ex", description = "Exit: Logout current user")  //works with built in exit function
		fun exit() {
			println("Logged out ")
			UserConsoleService.id = null
		}

		@Command(name="add-activity", abbrev="aa", description = "Add activity: create and add an activity for the logged in user")
		fun addActivity(
			@Param(name = "type") type: String,
			@Param(name = "location") location: String, @Param(name = "distance") distance: Float
		) {
			console.renderActivity(paceApi.addActivity(UserConsoleService.id!!, type, location, distance))
		}


		@Command(name="list-activities", abbrev="la", description = "List Activities: List all activities for logged in user")
		fun listActivities() {
			console.renderActivities(paceApi.getActivities(UserConsoleService.id!!))
		}

		@Command(name="add-location", abbrev="al", description = "Add location: Append location to an activity")
		fun addLocation(
			@Param(name = "activity-id") id: String,
			@Param(name = "longitude") longitude: Double, @Param(name = "latitude") latitude: Double
		) {
			val activity = paceApi.getActivity(UserConsoleService.id!!, id)
			if (activity != null) {
				if (paceApi.addLocation(UserConsoleService.id!!, activity.id, latitude, longitude)) {
					console.println("Location $latitude, $longitude added")
				} else {
					console.println("Error: Could not add location")
				}
			} else {
				console.println("Error: Activity not found")
			}
		}

		@Command(name="activity-report", abbrev="ar", description = "Activity Report: List all activities for logged in user, sorted alphabetically by type")
		fun activityReport() {
			console.println("Activity report sorted by type: ")
			console.renderActivities(
				paceApi.getActivities(UserConsoleService.id!!)?.sortedWith(
					compareBy(
						{ it.type },
						{ it.location })
				)
			)
		}

		@Command(name="activity-report", abbrev="ar", description = "Activity Report: List all activities for logged in user by type. Sorted longest to shortest distance")
		fun activityReport(@Param(name = "byType: type") type: String) {
			console.println("Activity report for type '" + type + "':")
			console.renderActivities(
				paceApi.getActivities(UserConsoleService.id!!, type)?.sortedWith(compareBy({ it.distance }))
			)
		}

		@Command(name="list-activity-locations", abbrev="lal", description = "List all locations for a specific activity")
		fun listActivityLocations(@Param(name = "activity-id") id: String) {
			val activity = paceApi.getActivity(UserConsoleService.id!!, id)
			if (activity != null) {
				console.renderActivity(activity);
				console.renderLocations(activity.route);
			} else {
				console.println("Activity not found")
			}
		}

		@Command(name="follow", abbrev="f", description = "Follow Friend: Follow a specific friend")
		fun follow(@Param(name = "email") email: String) {
			if (paceApi.followFriend(UserConsoleService.id!!, email)) {
				console.println("Now following " + email + ", you now have " + paceApi.listFriends(UserConsoleService.id!!)?.count() + " friend(s)")
			} else {
				console.println("Can't follow " + email + " (hint: check the email address is a valid user)")
			}
		}


		@Command(name="list-friends", abbrev="lf", description = "List Friends: List all of the friends of the logged in user")
		fun listFriends() {
			val friends = paceApi.listFriends(UserConsoleService.id!!)
			if (friends != null && friends.isNotEmpty()) {
				console.println("You have " + friends.count() + " friend(s):")
			}
			console.renderFriendUsers(friends)
		}

		@Command(name="unfollow-friend", abbrev="uf", description = "Unfollow Friends: Stop following a friend")
		fun unfollowFriend(@Param(name = "email") email: String) {
			if (paceApi.unfollowFriend(UserConsoleService.id!!, email)) {
				console.println("No longer following " + email)
			} else {
				console.println("Error: Not a friend of " + email)
			}
		}

		@Command(name="friend-activity-report", abbrev="far", description = "Friend Activity Report: List all activities of specific friend, sorted alphabetically by type)")
		fun friendActivityReport(@Param(name = "email") email: String) {
			console.renderActivities(
				paceApi.friendActivityReport(
					UserConsoleService.id!!,
					email
				)?.sortedWith(compareBy({ it.type }, { it.location }))
			)
		}

		@Command(name="message-friend", abbrev="mf", description = "Message Friend: send a message to a friend")
		fun messageFriend(
			@Param(name = "email") email: String,
			@Param(name = "message") message: String
		) {
			if (paceApi.messageFriend(UserConsoleService.id!!, email, message)) {
				console.println("Message sent")
			} else {
				console.println("Message not sent")
			}

		}

		@Command(name="list-messages", abbrev="lm", description = "List Messages: List all messages for the logged in user")
		fun listMessages() {
			console.renderMessages(paceApi.listMessages(UserConsoleService.id!!))
		}

		@Command(name="message-all-friends", abbrev="maf", description = "Message All Friends: send a message to all friends")
		fun messageAllFriends(@Param(name = "message") message: String) {
			if (paceApi.messageAllFriends(UserConsoleService.id!!, message)) {
				console.println("Message sent")
			} else {
				console.println("Message not sent as you may not have any friends")
			}
		}

		@Command(name="distance-leader-board", abbrev="dlb", description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
		fun distanceLeaderBoard() {
			console.println("distances of all friends, sorted longest to shortest:")
			console.renderLeaders(paceApi.getLeaderBoard(UserConsoleService.id!!)?.sortedWith(compareBy({ -it.distance })))
		}

		@Command(name="distance-leader-board-by-type", abbrev="dlbbt", description = "Distance Leader Board: distance leader board sorted by type")
		fun distanceLeaderBoardByType(@Param(name = "byType: type") type: String) {
			console.println("Distances of all friends by type '" + type + "':")
			console.renderLeaders(
				paceApi.getLeaderBoard(
					UserConsoleService.id!!,
					type
				)?.sortedWith(compareBy({it.distance}))?.reversed()
			)
		}

		@Command(name="location-leader-board", abbrev="llb", description = "Location Leader Board: list sorted summary distances of all friends in named location")
		fun locationLeaderBoard(@Param(name = "location") locale: String) {
			console.println("Distances of all friends by location '" + locale + "':")
			console.renderLeaders(
				paceApi.getLeaderBoard(
					UserConsoleService.id!!,
					locale = locale
				)?.sortedWith(compareBy({it.distance}))?.reversed()
			)
		}
	}
}
	