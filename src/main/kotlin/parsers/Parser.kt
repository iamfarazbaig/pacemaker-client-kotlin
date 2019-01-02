package parsers

import models.*

/**
 * Parser class to print line
 */
open class Parser {
	open fun println(s: String) {
		System.out.println(s)
	}

	open fun renderUser(user: User?) {
		System.out.println(user.toString())
	}

	open fun renderUsers(users: Collection<User>?) {
		System.out.println(users.toString())
	}

	open fun renderFriendUsers(users: Collection<User>?) {
		System.out.println(users.toString())
	}

	open fun renderActivity(activity: Activity?) {
		System.out.println(activity.toString())
	}

	open fun renderActivities(activities: Collection<Activity>?) {
		System.out.println(activities.toString())
	}

	open fun renderLocations(locations: List<Location>?) {
		System.out.println(locations.toString())
	}

	open fun renderMessages(messages: Collection<Message>?) {
		System.out.println(messages.toString())
	}

	open fun renderLeaders(leaders: Collection<Leader>?) {
		System.out.println(leaders.toString())
	}
}