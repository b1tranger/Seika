package tachiyomi.domain.manga.model

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MangaContentFilterTest {

    @Test
    fun `isAdultContent returns true for adult tags`() {
        val adultManga = Manga.create().copy(
            genre = listOf("Action", "Adult")
        )
        assertTrue(adultManga.isAdultContent())
    }

    @Test
    fun `isAdultContent returns true for Hentai tags`() {
        val adultManga = Manga.create().copy(
            genre = listOf("Hentai")
        )
        assertTrue(adultManga.isAdultContent())
    }

    @Test
    fun `isAdultContent returns true for NSFW tags`() {
        val adultManga = Manga.create().copy(
            genre = listOf("NSFW", "Romance")
        )
        assertTrue(adultManga.isAdultContent())
    }

    @Test
    fun `isAdultContent is case insensitive`() {
        val adultManga = Manga.create().copy(
            genre = listOf("pornographic")
        )
        assertTrue(adultManga.isAdultContent())
    }

    @Test
    fun `isAdultContent returns false for safe tags`() {
        val safeManga = Manga.create().copy(
            genre = listOf("Action", "Adventure", "Comedy")
        )
        assertFalse(safeManga.isAdultContent())
    }

    @Test
    fun `isAdultContent returns false for null genre`() {
        val manga = Manga.create().copy(genre = null)
        assertFalse(manga.isAdultContent())
    }

    @Test
    fun `isAdultContent returns false for empty genre`() {
        val manga = Manga.create().copy(genre = emptyList())
        assertFalse(manga.isAdultContent())
    }
}
