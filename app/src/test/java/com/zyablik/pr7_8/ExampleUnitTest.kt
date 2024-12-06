package com.zyablik.pr7_8

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_MANIFEST_NAME)
class ExampleUnitTest {
    private lateinit var mainActivity: MainActivity
    private lateinit var mainActivitySpy: MainActivity
    private lateinit var mockContext: Context
    private val testDispatcher = StandardTestDispatcher()
    private val imageView: ImageView = mock(ImageView::class.java)
    private val textView: TextView = mock(TextView::class.java)
    private val button: Button = mock(Button::class.java)

    @Before
    fun setUp(){
        mockContext = mock(Context::class.java)
        Dispatchers.setMain(testDispatcher)
        mainActivity = MainActivity()
        mainActivitySpy = spy(MainActivity())

    }

    @Test
    fun test_downloadGood() = runBlocking{
        val link = "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png"
        assertNotNull(mainActivity.downloadImage(link))
    }
    @Test
    fun test_downloadBad() = runBlocking{
        val link = "https://media.geeksforgeeks.org/wp-conteng/cdn-uploads/gfg_200x200-min.png"
        assertNull(mainActivity.downloadImage(link))
    }

    @Test
    fun test_initalize(){
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            val activity = controller.get()
            assertNotNull(activity)
            val txt = activity.findViewById<TextView>(R.id.txt)
            assertNotNull(txt)
            val button = activity.findViewById<Button>(R.id.button)
            assertNotNull(button)
        }
    }

    @Test
    fun test_isDownload_work(){
        val link = "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png"
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            Mockito.doAnswer{
                mainActivitySpy.downloadImage(link)
                null
            }.`when`(button).performClick()
            button.performClick()
            verify(mainActivitySpy).downloadImage(link)
        }
    }

    @Test
    fun test_doTextChanges(){
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            controller.get().findViewById<Button>(R.id.button).performClick()
            val texts: TextView = controller.get().findViewById(R.id.txt)
            Mockito.doAnswer{
                texts.text = "Done"
                null
            }.`when`(button).performClick()
            button.performClick()
            assertEquals("Done",texts.text)
        }
    }
}