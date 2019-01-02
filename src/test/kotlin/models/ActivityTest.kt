package models

import controllers.PacemakerAPI
import models.Fixtures.users
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityTest {

    var pacemaker = PacemakerAPI()
    var homer = User("homer", "simpson", "homer@simpson.com", "secret")

    @BeforeAll
    fun setup() {
        pacemaker.deleteUsers()
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password)!!
    }

    @AfterAll
    fun tearDown() {
    }

    /**
     * This tests the constructor
     */
    @Test
    fun testConstructor() {
        val homer = Activity("run", "park", 200F)
        assertEquals("run", homer.type)
        assertEquals("park", homer.location)
        assertEquals(200F, homer.distance)
    }

    /**
     * Testing equals() and == operator
     */
    @Test
    fun testEquality() {
        val homer = Activity("run", "park", 200F)
        val homerCopy = Activity("run", "park", 200F)
        val bert = Activity("swim", "pool", 300F)

        assertEquals(homer, homer)
        assertNotEquals(homer, homerCopy)
        assertNotEquals(homer, bert)
        assertNotSame(homer, homerCopy)
        assertNotSame(homer, bert)
    }

    /**
     * Testing if ids generated are all unique
     */
    @Test
    fun testIds() {
        val ids = HashSet<String>()
        users.forEach {
            ids.add(it.id)
        }
        assertEquals(users.size, ids.size)
    }

    /**
     * Testing toString()
     */
    @Test
    fun testToString() {
        val homer = Activity("run", "park", 200F)
        assertEquals(
            "Activity(type=${homer.type}, location=${homer.location}, distance=${homer.distance}, id=${homer.id}, route=${homer.route})",
            homer.toString()
        )
    }

    @Test
    fun testCreateActivity() {
        val (type, location, distance) = Activity("walk", "shop", 2.5F)

        val returnedActivity = pacemaker.addActivity(homer.id, type, location, distance)
        assertEquals(type, returnedActivity!!.type)
        assertEquals(location, returnedActivity.location)
        assertEquals(distance.toDouble(), returnedActivity.distance.toDouble(), "hi")
        assertNotNull(returnedActivity.id)
    }

    @Test
    fun testGetActivity() {
        val (type, location, distance) = Activity("run", "fridge", 0.5F)
        val returnedActivity1 = pacemaker.addActivity(homer.id, type, location, distance)
        val returnedActivity2 = pacemaker.getActivity(homer.id,returnedActivity1!!.id)
        assertEquals(returnedActivity1, returnedActivity2)
    }

    @Test
    fun testDeleteActivity() {
        val (type, location, distance) = Activity("sprint", "pub", 4.5F)
        var returnedActivity = pacemaker.addActivity(homer.id, type, location, distance)
//        assertNotNull(returnedActivity)
        pacemaker.deleteActivities(homer.id)
        returnedActivity = pacemaker.getActivity(homer.id, returnedActivity?.id!!)
        assertNull(returnedActivity)
    }
}