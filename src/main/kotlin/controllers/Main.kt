package controllers

import asg.cliche.*
import asg.cliche.*
import java.io.IOException

/**
 * This is the API URL
 */
val console = PacemakerConsoleService("https://dry-fortress-94636.herokuapp.com")

/**
 *This is the Main class of pacemaker-client-kotlin
 */
@Throws(IOException::class)
fun main(args: Array<String>) {
	ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions", console)
		.commandLoop()
} 