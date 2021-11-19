package app.shosetsu.lib

import app.shosetsu.lib.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * This file is part of shosetsu-services.
 * shosetsu-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * shosetsu-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with shosetsu-services.  If not, see https://www.gnu.org/licenses/.
 */

/**
 * shosetsu-services
 * 30 / May / 2019
 *
 * @author github.com/doomsdayrs
 * @noinspection unused
 */
@Suppress("unused")
interface IExtension {
	companion object {
		/** Chapter url key for [expandURL] & [shrinkURL]*/
		const val KEY_CHAPTER_URL: Int = 2

		/** Novel url key for [expandURL] & [shrinkURL]*/
		const val KEY_NOVEL_URL: Int = 1
	}

	/**
	 * @param id ID
	 * @param version 3 index array
	 * @param libVersion Version of the library this extension was made for
	 * @param author Author / Creator of this extension
	 * @param repo Repository URL that this extension is connected with
	 * @param dependencies
	 */
	@Serializable
	data class ExMetaData(
		@SerialName(J_ID)
		val id: Int,
		@SerialName(J_VERSION)
		val version: Version,
		@SerialName(J_LIB_VERSION)
		val libVersion: Version,
		@SerialName(J_AUTHOR)
		val author: String = "",
		@SerialName(J_REPO)
		val repo: String = "",
		@SerialName(J_DEP)
		val stringDep: List<String> = listOf()
	) {
		val dependencies: Map<String, Version> by lazy {
			val v = stringDep.map { value -> value.split(">=").let { dep -> dep.first() to dep[1] } }
			val map = HashMap<String, Version>()
			v.forEach { (dep, ver) ->
				map[dep] = Version(ver)
			}
			map
		}
	}

	/**
	 * This represents a "Page" that the source might have for listing novels.
	 *
	 * Many sources have a singular listing,
	 * which contains filters and queries.
	 *
	 * Some sources have extra pages to display sources, such as
	 * a separate page for latest updates or most viewed
	 */
	class Listing(
		/** Name of this listing */
		val name: String,

		/** If you can continue scrolling for more data or not */
		val isIncrementing: Boolean,

		/**
		 * This gets data from the listing
		 */
		val getListing: (data: Map<Int, *>) -> Array<Novel.Listing>
	)

	/** Meta data of the extension */
	val exMetaData: ExMetaData

	/** Name of this extension */
	val name: String

	/**
	 * Base URL of the extension
	 * Used to open up in browser
	 */
	val baseURL: String

	/**
	 * Image URL of the extension
	 * Used for user recognition
	 */
	val imageURL: String

	/**
	 * Formatter identification
	 * Separates this from other extensions
	 * Should be as unique as possible
	 */
	val formatterID: Int

	/** If this extension is capable of searching */
	val hasSearch: Boolean

	/** If this extensions search can be incremented */
	val isSearchIncrementing: Boolean

	/**
	 * If this extension has cloudflare protection that
	 * requires interception
	 */
	val hasCloudFlare: Boolean

	/**
	 * Represents the different listings of this extension
	 * @see [Listing]
	 */
	val listings: Array<Listing>

	/**
	 * Represents the settings model this extension provides
	 */
	val settingsModel: Array<Filter<*>>

	/**
	 * Represents the filters models to be adjusted on the right
	 */
	val searchFiltersModel: Array<Filter<*>>

	/** Represents the data type of the chapters that [getPassage] returns */
	val chapterType: Novel.ChapterType

	/**
	 * Specifies what index should be used when incrementing pages.
	 *
	 * This is useful as some websites follow 0..1..2 and some follow 1..2..3
	 */
	val startIndex: Int

	/**
	 * Applies a setting a value
	 */
	fun updateSetting(id: Int, value: Any?)

	/**
	 * @param data Data that includes query and other filters
	 */
	fun search(data: Map<Int, *>): Array<Novel.Listing>

	/**
	 * Get the passage of a novel
	 * @param chapterURL of the chapter,
	 * will be fed into [expandURL] with [KEY_CHAPTER_URL]
	 *
	 * @return [String] of data corresponding to the [chapterType]
	 */
	fun getPassage(chapterURL: String): ByteArray

	/**
	 * Using the novel url, Requests for information on the novel
	 * @param novelURL url of novel,
	 * will be fed into [expandURL] with [KEY_NOVEL_URL]
	 *
	 * @param loadChapters option to load chapters or not,
	 * for minor performance options (and debug)
	 */
	fun parseNovel(novelURL: String, loadChapters: Boolean): Novel.Info

	/**
	 *  @param smallURL URL to enlarge
	 *  @param type Type of url. Either [KEY_CHAPTER_URL] or [KEY_NOVEL_URL]
	 *  @return enlarged URL
	 */
	fun expandURL(smallURL: String, type: Int): String

	/**
	 * Access class to shrink data of novels
	 * @param longURL A long url to shrink
	 * @param type Type of url. Either [KEY_CHAPTER_URL] or [KEY_NOVEL_URL]
	 * @return shrunken url
	 */
	fun shrinkURL(longURL: String, type: Int): String
}
