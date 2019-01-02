package controllers

import com.google.gson.GsonBuilder
import models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *Retrofit interface
 */
internal interface PacemakerInterface {
    @GET("/users")
    fun getUsers(@Query("id") id: String? = null, @Query("email") email: String? = null): Call<List<User>>

    @POST("/users")
    fun registerUser(@Body User: User): Call<User>

    @DELETE("/users")
    fun deleteUsers(): Call<String>

    @GET("/users/{id}")
    fun getUser(@Path("id") id: String): Call<User>

    @PUT("/users/{id}")
    fun updateUser(@Path("id") id: String, @Body User: User): Call<User>

    @DELETE("/users/{id}")
    fun deleteUser(@Path("id") id: String): Call<String>

    @POST("/users/{id}/activities")
    fun addActivity(@Path("id") id: String, @Body activity: Activity): Call<Activity>

    @GET("/users/{id}/activities/{activityId}")
    fun getActivity(@Path("id") id: String, @Path("activityId") activityId: String): Call<Activity>

    @GET("/users/{id}/activities")
    fun getActivities(@Path("id") id: String, @Query("type") type: String? = null): Call<List<Activity>>

    @GET("/users/{id}/friends/{email}/activities")
    fun getFriendActivities(@Path("id") id: String, @Path("email") friendEmail: String): Call<List<Activity>>

    @DELETE("/users/{id}/activities")
    fun deleteActivities(@Path("id") id: String): Call<String>

    @POST("/users/{id}/activities/{activityId}/locations")
    fun addLocation(
            @Path("id") id: String, @Path("activityId") activityId: String,
            @Body location: Location
    ): Call<Location>

    @GET("/users/{id}/friends/")
    fun listFriends(@Path("id") id: String): Call<List<User>>

    @POST("/users/{id}/friends/{email}")
    fun followFriend(@Path("id") id: String, @Path("email") friendEmail: String): Call<String>

    @DELETE("/users/{id}/friends/{email}")
    fun unfollowFriend(@Path("id") id: String, @Path("email") friendEmail: String): Call<String>

    @POST("/users/{id}/messages/{email}")
    fun messageFriend(@Path("id") id: String, @Path("email") friendEmail: String, @Body message: Message): Call<String>

    @GET("/users/{id}/messages")
    fun listMessages(@Path("id") id: String): Call<List<Message>>

    @POST("/users/{id}/messages/")
    fun messageAllFriends(@Path("id") id: String, @Body message: Message): Call<String>
}

/**
 * This is the PacemakerAPI class
 */
class PacemakerAPI(url: String = "https://dry-fortress-94636.herokuapp.com") {
    internal var pacemakerInterface: PacemakerInterface

    /**
     * Initialization
     */
    init {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
        pacemakerInterface = retrofit.create(PacemakerInterface::class.java)
    }

    /**
     * create user function
     */
    fun createUser(firstName: String, lastName: String, email: String, password: String): User? {
        var returnedUser: User? = null
        try {
            val call = pacemakerInterface.registerUser(User(firstName, lastName, email, password))
            val response = call.execute()
            returnedUser = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return returnedUser
    }

    /**
     * get user function
     */
    fun getUser(id: String): User? {
        var user: User? = null
        try {
            val call = pacemakerInterface.getUsers(id = id)
            val response = call.execute()
            val userList = response.body()
            if (userList != null && userList.isNotEmpty()) {
                user = userList.first()
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return user
    }

    /**
     * get users function
     */
    fun getUsers(): Collection<User>? {
        var users: Collection<User>? = null
        try {
            val call = pacemakerInterface.getUsers()
            val response = call.execute()
            users = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return users
    }

    /**
     * get user by email function
     */
    fun getUserByEmail(email: String): User? {
        var user: User? = null
        try {
            val call = pacemakerInterface.getUsers(email = email)
            val response = call.execute()
            val userList = response.body()
            if (userList != null && userList.isNotEmpty()) {
                user = userList.first()
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return user
    }

    /**
     * update user function
     */
    fun updateUser(
            id: String,
            firstName: String,
            lastName: String,
            email: String,
            password: String,
            disabled: Boolean,
            admin: Boolean
    ): User? {
        var returnedUser: User? = null
        try {
            val call = pacemakerInterface.updateUser(
                    id,
                    User(
                            firstname = firstName,
                            lastname = lastName,
                            email = email,
                            password = password,
                            disabled = disabled,
                            admin = admin
                    )
            )
            val response = call.execute()
            returnedUser = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return returnedUser
    }

    /**
     * delete user function
     */
    fun deleteUser(id: String): Boolean {
        try {
            val call = pacemakerInterface.deleteUser(id)
            if (call.execute().code() == 204) {
                return true
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * delete users
     */
    fun deleteUsers(): Boolean {
        try {
            val call = pacemakerInterface.deleteUsers()
            if (call.execute().code() == 204) {
                return true
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * add location function
     */
    fun addLocation(id: String, activityId: String, latitude: Double, longitude: Double): Boolean {
        try {
            val call = pacemakerInterface.addLocation(id, activityId, Location(latitude, longitude))
            if (call.execute().code() == 200) {
                return true
            }

        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * add activity method
     */
    fun addActivity(id: String, type: String, location: String, distance: Float): Activity? {
        var returnedActivity: Activity? = null
        try {
            val call = pacemakerInterface.addActivity(id, Activity(type, location, distance))
            val response = call.execute()
            returnedActivity = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return returnedActivity
    }

    /**
     * get activity function
     */
    fun getActivity(userId: String, activityId: String): Activity? {
        var activity: Activity? = null
        try {
            val call = pacemakerInterface.getActivity(userId, activityId)
            val response = call.execute()
            activity = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return activity
    }

    /**
     * get activities function
     */
    fun getActivities(id: String, type: String? = null): Collection<Activity>? {
        var activities: Collection<Activity>? = null
        try {
            val call = pacemakerInterface.getActivities(id, type)
            val response = call.execute()
            activities = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return activities
    }

    /**
     * delete activities function
     */
    fun deleteActivities(id: String): Boolean {
        try {
            val call = pacemakerInterface.deleteActivities(id)
            if (call.execute().code() == 204) {
                return true
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * list friends function
     */
    fun listFriends(id: String): Collection<User>? {
        var users: Collection<User>? = null
        try {
            val call = pacemakerInterface.listFriends(id)
            val response = call.execute()
            users = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return users
    }

    /**
     * follow friend function
     */
    fun followFriend(id: String, email: String): Boolean {
        try {
            val call = pacemakerInterface.followFriend(id, email)
            if (call.execute().code() == 204)
                return true
        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * unfollow friend
     */
    fun unfollowFriend(id: String, email: String): Boolean {
        try {
            val call = pacemakerInterface.unfollowFriend(id, email)
            if (call.execute().code() == 204) {
                return true
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * friend activity report
     */
    fun friendActivityReport(id: String, email: String): Collection<Activity>? {
        var activities: Collection<Activity>? = null
        try {
            val call = pacemakerInterface.getFriendActivities(id, email)
            val response = call.execute()
            activities = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return activities
    }

    /**
     * send message function
     */
    fun messageFriend(id: String, email: String, message: String): Boolean {
        try {
            val call = pacemakerInterface.messageFriend(id, email, Message(message, id))
            if (call.execute().code() == 204) {
                return true
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * list messages
     */
    fun listMessages(id: String): Collection<Message>? {
        var messages: Collection<Message>? = null
        try {
            val call = pacemakerInterface.listMessages(id)
            val response = call.execute()
            messages = response.body()
        } catch (e: Exception) {
            println("Error!")
        }
        return messages
    }

    /**
     * message all friends function
     */
    fun messageAllFriends(id: String, message: String): Boolean {
        try {
            val call = pacemakerInterface.messageAllFriends(id, Message(message, id))
            if (call.execute().code() == 204) {
                return true
            }
        } catch (e: Exception) {
            println("Error!")
        }
        return false
    }

    /**
     * leader board function
     */
    fun getLeaderBoard(id: String, type: String? = null, locale: String? = null): Collection<Leader>? {
        val leaders: MutableList<Leader>? = ArrayList()
        val friendlist = listFriends(id)
        if (friendlist != null) {
            for (friend in friendlist) {
                var activitieslist = friendActivityReport(
                        id,
                        friend.email
                )
                if (activitieslist != null) {
                    if (type != null) {
                        activitieslist = activitieslist.filter { it.type == type }
                    }
                    if (locale != null) {
                        activitieslist = activitieslist.filter { it.location == locale }
                    }

                    leaders?.add(Leader(
                            friend.id,
                            friend.firstname,
                            friend.lastname,
                            friend.email,
                            activitieslist.sumByDouble { it.distance.toDouble() })
                    )
                }
            }
        }
        return leaders
    }


}