package eu.kanade.domain.source.model

import dev.icerock.moko.resources.StringResource
import tachiyomi.i18n.MR

enum class ExtensionFilterMode(val titleRes: StringResource) {
    BLOCK_ALL_18_PLUS(MR.strings.pref_extension_filter_mode_nsfw),
    ALLOW_SELECTED(MR.strings.pref_extension_filter_mode_whitelist),
    BLOCK_ALL(MR.strings.pref_extension_filter_mode_all),
}
