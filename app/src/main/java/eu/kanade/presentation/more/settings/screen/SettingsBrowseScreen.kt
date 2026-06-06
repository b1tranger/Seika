package eu.kanade.presentation.more.settings.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.domain.source.service.SourcePreferences
import eu.kanade.presentation.more.settings.Preference
import eu.kanade.presentation.more.settings.screen.browse.ExtensionStoresScreen
import eu.kanade.tachiyomi.util.system.AuthenticatorUtil.authenticate
import eu.kanade.tachiyomi.core.content.ContentPreferences
import eu.kanade.tachiyomi.ui.browse.extension.whitelist.ExtensionWhitelistScreen
import mihon.domain.extension.interactor.GetExtensionStoreCountAsFlow
import tachiyomi.core.common.i18n.stringResource
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.pluralStringResource
import tachiyomi.presentation.core.i18n.stringResource
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object SettingsBrowseScreen : SearchableSettings {

    @ReadOnlyComposable
    @Composable
    override fun getTitleRes() = MR.strings.browse

    @Composable
    override fun getPreferences(): List<Preference> {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

        val sourcePreferences = remember { Injekt.get<SourcePreferences>() }
        val contentPreferences = remember { Injekt.get<ContentPreferences>() }
        val getExtensionStoreCountAsFlow = remember { Injekt.get<GetExtensionStoreCountAsFlow>() }

        val reposCount by getExtensionStoreCountAsFlow().collectAsState(0)

        val filterModeEntries = remember {
            eu.kanade.domain.source.model.ExtensionFilterMode.entries
                .associateWith { context.stringResource(it.titleRes) }
        }

        return listOf(
            Preference.PreferenceGroup(
                title = stringResource(MR.strings.label_sources),
                preferenceItems = listOf(
                    Preference.PreferenceItem.SwitchPreference(
                        preference = sourcePreferences.hideInLibraryItems,
                        title = stringResource(MR.strings.pref_hide_in_library_items),
                    ),
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(MR.strings.extensionStores),
                        subtitle = pluralStringResource(MR.plurals.num_repos, reposCount.toInt(), reposCount),
                        onClick = {
                            navigator.push(ExtensionStoresScreen())
                        },
                    ),
                ),
            ),
            Preference.PreferenceGroup(
                title = stringResource(MR.strings.pref_category_nsfw_content),
                preferenceItems = listOf(
                    Preference.PreferenceItem.ListPreference(
                        preference = sourcePreferences.extensionFilterMode,
                        entries = filterModeEntries,
                        title = stringResource(MR.strings.pref_extension_filter_mode),
                    ),
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(MR.strings.extension_whitelist),
                        onClick = { navigator.push(ExtensionWhitelistScreen()) },
                        enabled = sourcePreferences.extensionFilterMode.get() == eu.kanade.domain.source.model.ExtensionFilterMode.ALLOW_SELECTED,
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        preference = contentPreferences.enableStrictContentFilter,
                        title = stringResource(MR.strings.pref_enable_strict_content_filter),
                        subtitle = stringResource(MR.strings.pref_enable_strict_content_filter_summary),
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        preference = sourcePreferences.showNsfwSource,
                        title = stringResource(MR.strings.pref_show_nsfw_source),
                        subtitle = stringResource(MR.strings.requires_app_restart),
                        onValueChanged = {
                            (context as FragmentActivity).authenticate(
                                title = context.stringResource(MR.strings.pref_category_nsfw_content),
                            )
                        },
                    ),
                    Preference.PreferenceItem.InfoPreference(stringResource(MR.strings.parental_controls_info)),
                ),
            ),
        )
    }
}
