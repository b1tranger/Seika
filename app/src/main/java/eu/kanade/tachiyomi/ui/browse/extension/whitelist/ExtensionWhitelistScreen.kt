package eu.kanade.tachiyomi.ui.browse.extension.whitelist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.domain.source.service.SourcePreferences
import eu.kanade.presentation.browse.components.BaseBrowseItem
import eu.kanade.presentation.browse.components.ExtensionIcon
import eu.kanade.presentation.components.AppBar
import eu.kanade.presentation.components.AppBarTitle
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.extension.ExtensionManager
import eu.kanade.tachiyomi.extension.model.Extension
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.FastScrollLazyColumn
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.components.material.topSmallPaddingValues
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.screens.LoadingScreen
import tachiyomi.presentation.core.util.plus
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ExtensionWhitelistScreen : Screen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { ExtensionWhitelistScreenModel() }
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                AppBar(
                    title = stringResource(MR.strings.extension_whitelist),
                    navigateUp = navigator::pop,
                )
            },
        ) { contentPadding ->
            if (state.isLoading) {
                LoadingScreen()
                return@Scaffold
            }

            ExtensionWhitelistContent(
                state = state,
                contentPadding = contentPadding,
                onClickItem = screenModel::toggleWhitelist,
            )
        }
    }

    @Composable
    private fun ExtensionWhitelistContent(
        state: ExtensionWhitelistState,
        contentPadding: PaddingValues,
        onClickItem: (String) -> Unit,
    ) {
        FastScrollLazyColumn(
            contentPadding = contentPadding + topSmallPaddingValues,
        ) {
            items(
                items = state.extensions,
                key = { it.pkgName },
            ) { extension ->
                val isChecked = state.whitelist.contains(extension.pkgName)
                BaseBrowseItem(
                    onClickItem = { onClickItem(extension.pkgName) },
                    icon = {
                        ExtensionIcon(
                            extension = extension,
                            modifier = Modifier.padding(MaterialTheme.padding.extraSmall),
                        )
                    },
                    action = {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onClickItem(extension.pkgName) },
                        )
                    },
                ) {
                    AppBarTitle(
                        title = extension.name,
                        subtitle = extension.pkgName,
                    )
                }
            }
        }
    }
}

class ExtensionWhitelistScreenModel(
    private val preferences: SourcePreferences = Injekt.get(),
    private val extensionManager: ExtensionManager = Injekt.get(),
) : StateScreenModel<ExtensionWhitelistState>(ExtensionWhitelistState()) {

    init {
        screenModelScope.launch {
            combine(
                extensionManager.installedExtensionsFlow,
                extensionManager.untrustedExtensionsFlow,
                preferences.extensionFilterWhitelist.changes(),
            ) { installed, untrusted, whitelist ->
                (installed + untrusted).sortedBy { it.name } to whitelist
            }.collect { (extensions, whitelist) ->
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        extensions = extensions,
                        whitelist = whitelist,
                    )
                }
            }
        }
    }

    fun toggleWhitelist(pkgName: String) {
        val current = preferences.extensionFilterWhitelist.get()
        preferences.extensionFilterWhitelist.set(
            if (current.contains(pkgName)) {
                current - pkgName
            } else {
                current + pkgName
            },
        )
    }
}

data class ExtensionWhitelistState(
    val isLoading: Boolean = true,
    val extensions: List<Extension> = emptyList(),
    val whitelist: Set<String> = emptySet(),
)
