package models

import java.util.ArrayList
import java.util.Arrays

object Fixtures {
  var users:List<User> = ArrayList(Arrays.asList(User("marge", "simpson", "marge@simpson.com", "secret"),
                                                 User("lisa", "simpson", "lisa@simpson.com", "secret"),
                                                 User("bart", "simpson", "bart@simpson.com", "secret"),
                                                 User("maggie", "simpson", "maggie@simpson.com", "secret")))
	
  var activities:List<Activity> = ArrayList(
    Arrays.asList(Activity("walk", "fridge", 0.001F),
                  Activity("walk", "bar", 1.0F),
                  Activity("run", "work", 2.2F),
                  Activity("walk", "shop", 2.5F),
                  Activity("cycle", "school", 4.5F)))
	
  var locations:List<Location> = ArrayList(Arrays.asList(Location(23.3, 33.3),
                                                         Location(34.4, 45.2), Location(25.3, 34.3), Location(44.4, 23.3)))
}