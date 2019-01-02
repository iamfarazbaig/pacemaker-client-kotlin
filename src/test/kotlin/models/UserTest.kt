package models

import controllers.PacemakerAPI
import models.Fixtures.users
import java.util.*

/**
 * This is the test class for User in the models package
 */
class UserTest {
    internal var pacemaker = PacemakerAPI() //uses default url
    internal var homer = User("homer", "simpson", "homer@simpson.com", "secret")

    /**
     * This tests the constructor
     */
    @org.junit.jupiter.api.Test
    fun testConstructor() {
        val homer = User("homer", "simpson", "homer@simpson.com", "secret")
        kotlin.test.assertEquals("homer", homer.firstname)
        kotlin.test.assertEquals("simpson", homer.lastname)
        kotlin.test.assertEquals("homer@simpson.com", homer.email)
        kotlin.test.assertEquals("secret", homer.password)
        kotlin.test.assertFalse(homer.disabled)
        kotlin.test.assertFalse(homer.admin)
    }

    /**
     * Testing equals() and == operator
     */
    @org.junit.jupiter.api.Test
    fun testEquality() {
        val homer = User("homer", "simpson", "homer@simpson.com", "secret")
        val homerCopy = User("homer", "simpson", "homer@simpson.com", "secret")
        val bert = User("bert", "simpson", "bartr@simpson.com", "secret")

        kotlin.test.assertEquals(homer, homer)
        kotlin.test.assertNotEquals(homer, homerCopy)
        kotlin.test.assertNotEquals(homer, bert)
        kotlin.test.assertNotSame(homer, homerCopy)
        kotlin.test.assertNotSame(homer, bert)
    }

    /**
     * Testing if ids generated are all unique
     */
    @org.junit.jupiter.api.Test
    fun testIds() {
        val ids = HashSet<String>()
        users.forEach {
            ids.add(it.id)
        }
        kotlin.test.assertEquals(users.size, ids.size)
    }

    /**
     * Testing toString()
     */
    @org.junit.jupiter.api.Test
    fun testToString() {
        val homer = User("homer", "simpson", "homer@simpson.com", "secret")
        kotlin.test.assertEquals(
            "User(firstname=${homer.firstname}, lastname=${homer.lastname}, email=${homer.email}, password=${homer.password}, id=${homer.id}, friend=${homer.friend}, disabled=${homer.disabled}, admin=${homer.admin})",
            homer.toString()
        )
    }

}