package org.latinkotlinproject

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.latinkotlinproject.services.LatinCache
import org.latinkotlinproject.services.PerseusService

private val perseusTestCache = LatinCache()
private val perseusTestService = PerseusService()
private val mockPerseusRequest: (String) -> JsonObject = mockk()

@BeforeAll
fun setUp(): Unit {
  clearAllMocks()
}

@DisplayName("Perseus Tests")
class PerseusTests {

  @Nested
  @DisplayName("Cache Tests")
  inner class LatinCacheTests {

    private val testWord: String = "examen"
    private val cachedTestValue: JsonObject = JsonParser
      .parseString("{\"value\" : \"$testWord\"}").asJsonObject

    @AfterEach
    fun clearTestCache(): Unit {
      perseusTestCache.clearCache()
      assertTrue(perseusTestCache.isEmpty(), "Test cache is empty.")
    }

    @Test
    fun testCachingValues() {

      every {
        mockPerseusRequest(testWord)
      } answers { cachedTestValue }

      val result1 = perseusTestCache.getFromCacheOrFetch(testWord, { mockPerseusRequest(testWord) }, 5_000)
      verify(exactly = 1) { mockPerseusRequest(testWord) }

      val result2 = perseusTestCache.getFromCacheOrFetch(testWord, { mockPerseusRequest(testWord) }, 5_000)
      verify(exactly = 1) { mockPerseusRequest(testWord) }

      assertFalse(perseusTestCache.isEmpty(), "Test cache contains stored values.")
      assertEquals(cachedTestValue, result2)
    }
  }
}