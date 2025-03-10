package dev.mambo.lib.local

import android.R.attr.description
import android.content.Context
import android.provider.SyncStateContract.Helpers.update
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mambo.lib.local.dao.TaskDAO
import dev.mambo.lib.local.database.ActionaryDatabase
import dev.mambo.lib.local.entities.TaskEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class TaskDAOTest {
    private lateinit var dao: TaskDAO
    private lateinit var database: ActionaryDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ActionaryDatabase::class.java).build()
        dao = database.taskDAO()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insert_should_saves_task() =
        runTest {
            val task =
                TaskEntity(
                    title = "task",
                    description = "description",
                    createdAt = System.currentTimeMillis(),
                )
            val id = dao.insert(item = task)
            val item = dao.fetchById(id = id.toInt())
            assertNotNull(item)
            assertEquals(task.title, item.title)
            assertEquals(task.description, item.description)
        }

    @Test
    fun fetch_should_retrieve_the_correct_task() =
        runTest {
            val tasks =
                (1..10).map {
                    TaskEntity(
                        title = "task $it",
                        description = "description $it",
                        createdAt = System.currentTimeMillis(),
                    )
                }
            dao.insert(items = tasks)
            val values = dao.fetchAll()
            assertTrue { values.isNotEmpty() }
            val expected = values.first()
            val actual = dao.fetchById(id = expected.id)
            assertEquals(expected, actual)
            assertNotNull(actual)
            assertEquals(expected.title, actual.title)
            assertEquals(expected.description, actual.description)
        }

    @Test
    fun update_should_change_values_of_a_task() =
        runTest {
            val task =
                TaskEntity(
                    title = "task",
                    description = "description",
                    createdAt = System.currentTimeMillis(),
                )
            val id = dao.insert(item = task)
            val initial = dao.fetchById(id = id.toInt())
            assertNotNull(initial)
            val expected = initial.copy(title = "title 2", updatedAt = System.currentTimeMillis())
            dao.update(expected)
            val actual = dao.fetchById(id = id.toInt())
            assertNotNull(actual)
            assertEquals(expected.title, actual.title)
            assertEquals(expected.updatedAt, actual.updatedAt)
            assertEquals(expected.updatedAt, actual.updatedAt)
            assertNotEquals(expected.createdAt, actual.updatedAt)
        }

    @Test
    fun delete_should_remove_a_task() =
        runTest {
            val task =
                TaskEntity(
                    title = "task",
                    description = "description",
                    createdAt = System.currentTimeMillis(),
                )
            val id = dao.insert(item = task)
            val initial = dao.fetchById(id = id.toInt())
            assertNotNull(initial)
            dao.delete(initial)
            val actual = dao.fetchById(id = id.toInt())
            assertNull(actual)
        }
}
