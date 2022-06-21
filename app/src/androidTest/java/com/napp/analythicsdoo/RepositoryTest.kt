package com.napp.analythicsdoo

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private val repository: Repository = Repository.build()

    @Test
    fun getDefunktTest() {
        runBlocking {
            val result = repository.call(IRepo::class.java) {
                it.getUserRepoList()
            }
            assertEquals(User( "defunkt", 2), result)
        }
    }
}