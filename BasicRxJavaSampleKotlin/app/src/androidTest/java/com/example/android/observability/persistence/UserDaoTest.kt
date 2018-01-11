/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.observability.persistence

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test the implementation of [UserDao]
 */
@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: UsersDatabase

    @Before fun initDb() {
        // using an in-memory database because the information stored here disappears after test
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                UsersDatabase::class.java)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build()
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun getUsersWhenNoUserInserted() {
        database.userDao().getUserById("123")
                .test()
                .assertNoValues()
    }

    @Test fun insertAndGetUser() {
        // When inserting a new user in the data source
        database.userDao().insertUser(USER)

        // When subscribing to the emissions of the user
        database.userDao().getUserById(USER.id)
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue { it.id == USER.id && it.userName == USER.userName }
    }

    @Test fun updateAndGetUser() {
        // Given that we have a user in the data source
        database.userDao().insertUser(USER)

        // When we are updating the name of the user
        val updatedUser = User(USER.id, "new username", false)
        database.userDao().insertUser(updatedUser)

        // When subscribing to the emissions of the user
        database.userDao().getUserById(USER.id)
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue { it.id == USER.id && it.userName == "new username" && !it.isActive}
    }

    @Test fun deleteAndGetUser() {
        // Given that we have a user in the data source
        database.userDao().insertUser(USER)

        //When we are deleting all users
        database.userDao().deleteAllUsers()
        // When subscribing to the emissions of the user
        database.userDao().getUserById(USER.id)
                .test()
                // check that there's no user emitted
                .assertNoValues()
    }

    @Test fun getsUserIsActive() {
        // Given that we have a user in the data source
        database.userDao().insertUser(USER)

        // When we ask if the user is active
        val isActiveRequest: Single<Boolean> = database.userDao().isUserActive(USER.id)

        // Then the database should response with true
        isActiveRequest
                .test()
                .assertValue(true)
    }

    @Test fun getsUserIsNotActive() {
        // Given that we have a user in the data source
        database.userDao().insertUser(USER.copy(isActive = false))

        // When we ask if the user is active
        val isActiveRequest: Single<Boolean> = database.userDao().isUserActive(USER.id)

        // Then the database should response with true
        isActiveRequest
                .test()
                .assertValue(false)
    }

    companion object {
        private val USER = User("id", "username", true)
    }
}
