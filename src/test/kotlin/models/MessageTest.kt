package models


import models.Fixtures.users
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.*

class MessageTest {
    /**
     * This tests the constructor
     */
    @Test
    fun testConstructor() {
        val homer = Message("hi", "user@user")
        assertEquals("hi", homer.message)
        assertEquals("user@user", homer.from)
    }

    /**
     * Testing equals() and == operator
     */
    @Test
    fun testEquality() {
        val homer = Message("hi", "user@user")
        val homerCopy = Message("hi", "user@user")
        val bert = Message("hello", "bert@user")

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
        val homer = Message("hi", "user@user")
        assertEquals(
            "Message(message=${homer.message}, from=${homer.from}, id=${homer.id})",
            homer.toString()
        )
    }
}

