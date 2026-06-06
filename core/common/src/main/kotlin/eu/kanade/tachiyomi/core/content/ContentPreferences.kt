package eu.kanade.tachiyomi.core.content

import tachiyomi.core.common.preference.Preference
import tachiyomi.core.common.preference.PreferenceStore

class ContentPreferences(
    preferenceStore: PreferenceStore,
) {
    val enableStrictContentFilter: Preference<Boolean> = preferenceStore.getBoolean(
        "enable_strict_content_filter",
        true,
    )
}
