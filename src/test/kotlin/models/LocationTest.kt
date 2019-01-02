package models

import models.Fixtures.users
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.*

class LocationTest {
    /**
     * This tests the constructor
     */
    @Test
    fun testConstructor() {
        val homer = Location(3.0, 5.0)
        assertEquals(3.0, homer.latitude)
        assertEquals(5.0, homer.longitude)
    }

    /**
     * Testing equality
     */
    @Test
    fun testEquality() {
        val homer = Location(3.0, 5.0)
        val homerCopy = Location(3.0, 5.0)
        val bert = Location(1.0, 6.0)

        assertEquals(homer, homer)
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
        val homer = Location(3.0, 5.0)
        assertEquals(
            "Location(latitude=${homer.latitude}, longitude=${homer.longitude})",
            homer.toString()
        )
    }
}

