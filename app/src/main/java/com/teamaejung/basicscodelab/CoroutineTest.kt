package com.teamaejung.basicscodelab

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.PrintStream
import kotlin.system.measureTimeMillis

class SyncTest {
        fun main(): List<String> {
            val printedLines = capturePrintln {
                val time = measureTimeMillis {
                    runBlocking {
                        println("Weather forecast")
                        printForecast()
                        printTemperature()
                    }
                }
                println("Execution time: ${time / 1000.0} seconds")
            }
            return printedLines
        }
    suspend fun printForecast() {
        delay(1000)
        println("Sunny")
    }

    suspend fun printTemperature() {
        delay(1000)
        println("30\u00b0C")
    }

    fun capturePrintln(block: () -> Unit): List<String> {
        val printedLines = mutableListOf<String>()
        val oldOut = System.out
        val printStream = object : PrintStream(oldOut) {
            override fun println(x: Any?) {
                printedLines.add(x.toString())
                super.println(x)
            }
        }
        System.setOut(printStream)
        block()
        System.setOut(oldOut)
        return printedLines
    }
}

class AsyncTest {

    fun main(): List<String> {
        val printedLines = capturePrintln {
            runBlocking {
                println("Weather forecast")
                println(getWeatherReport())
                println("Have a good day!")
            }
        }
        return printedLines
    }

    suspend fun getWeatherReport() = coroutineScope {
        val forecast = async { getForecast() }
        val temperature = async { getTemperature() }
        "${forecast.await()} ${temperature.await()}"
    }

    suspend fun getForecast(): String {
        delay(1000)
        return "Sunny"
    }

    suspend fun getTemperature(): String {
        delay(1000)
        return "30\u00b0C"
    }
    fun capturePrintln(block: () -> Unit): List<String> {
        val printedLines = mutableListOf<String>()
        val oldOut = System.out
        val printStream = object : PrintStream(oldOut) {
            override fun println(x: Any?) {
                printedLines.add(x.toString())
                super.println(x)
            }
        }
        System.setOut(printStream)
        block()
        System.setOut(oldOut)
        return printedLines
    }
}

class ExceptionTest {

    fun main(): List<String> {
        val printedLines = capturePrintln {
            runBlocking {
                println("Weather forecast")
                println(getWeatherReport())
                println("Have a good day!")
            }
        }
        return printedLines
    }

    suspend fun getWeatherReport() = coroutineScope {
        val forecast = async { getForecast() }
        val temperature = async {
            try {
                getTemperature()
            } catch (e: AssertionError) {
                println("Caught exception $e")
                "{ No temperature found }"
            }
        }

        "${forecast.await()} ${temperature.await()}"
    }

    suspend fun getForecast(): String {
        delay(1000)
        return "Sunny"
    }

    suspend fun getTemperature(): String {
        delay(1000)
        return "30\u00b0C"
    }

    fun capturePrintln(block: () -> Unit): List<String> {
        val printedLines = mutableListOf<String>()
        val oldOut = System.out
        val printStream = object : PrintStream(oldOut) {
            override fun println(x: Any?) {
                printedLines.add(x.toString())
                super.println(x)
            }
        }
        System.setOut(printStream)
        block()
        System.setOut(oldOut)
        return printedLines
    }
}

class CancleTest {

    fun main(): List<String> {
        val printedLines = capturePrintln {
            runBlocking {
                println("Weather forecast")
                println(getWeatherReport())
                println("Have a good day!")
            }
        }
        return printedLines
    }

    suspend fun getWeatherReport() = coroutineScope {
        val forecast = async { getForecast() }
        val temperature = async { getTemperature() }

        delay(200)
        temperature.cancel()

        "${forecast.await()}"
    }

    suspend fun getForecast(): String {
        delay(1000)
        return "Sunny"
    }

    suspend fun getTemperature(): String {
        delay(1000)
        return "30\u00b0C"
    }

    fun capturePrintln(block: () -> Unit): List<String> {
        val printedLines = mutableListOf<String>()
        val oldOut = System.out
        val printStream = object : PrintStream(oldOut) {
            override fun println(x: Any?) {
                printedLines.add(x.toString())
                super.println(x)
            }
        }
        System.setOut(printStream)
        block()
        System.setOut(oldOut)
        return printedLines
    }
}

class DispatcherTest {

    fun main(): List<String> {
        val printedLines = capturePrintln {
            runBlocking {
                println("${Thread.currentThread().name} - runBlocking function")
                launch {
                    println("${Thread.currentThread().name} - launch function")
                    withContext(Dispatchers.Default) {
                        println("${Thread.currentThread().name} - withContext function")
                        delay(1000)
                        println("10 results found.")
                    }
                    println("${Thread.currentThread().name} - end of launch function")
                }
                println("Loading...")
            }
        }
        return printedLines
    }

    fun capturePrintln(block: () -> Unit): List<String> {
        val printedLines = mutableListOf<String>()
        val oldOut = System.out
        val printStream = object : PrintStream(oldOut) {
            override fun println(x: Any?) {
                printedLines.add(x.toString())
                super.println(x)
            }
        }
        System.setOut(printStream)
        block()
        System.setOut(oldOut)
        return printedLines
    }
}



