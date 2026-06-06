package eu.kanade.domain.extension.interactor

import eu.kanade.domain.extension.model.Extensions
import eu.kanade.domain.source.model.ExtensionFilterMode
import eu.kanade.domain.source.service.SourcePreferences
import eu.kanade.tachiyomi.extension.ExtensionManager
import eu.kanade.tachiyomi.extension.model.Extension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetExtensionsByType(
    private val preferences: SourcePreferences,
    private val extensionManager: ExtensionManager,
) {

    fun subscribe(): Flow<Extensions> {
        return combine(
            preferences.enabledLanguages.changes(),
            preferences.extensionFilterMode.changes(),
            preferences.extensionFilterWhitelist.changes(),
            extensionManager.installedExtensionsFlow,
            extensionManager.untrustedExtensionsFlow,
            extensionManager.availableExtensionsFlow,
        ) { flows ->
            val enabledLanguages = flows[0] as Set<String>
            val filterMode = flows[1] as ExtensionFilterMode
            val whitelist = flows[2] as Set<String>
            val _installed = flows[3] as List<Extension.Installed>
            val _untrusted = flows[4] as List<Extension.Untrusted>
            val _available = flows[5] as List<Extension.Available>

            val isExtensionAllowed: (Extension) -> Boolean = { extension ->
                !extension.isNsfw || when (filterMode) {
                    ExtensionFilterMode.BLOCK_ALL -> false
                    ExtensionFilterMode.BLOCK_ALL_18_PLUS -> false
                    ExtensionFilterMode.ALLOW_SELECTED -> whitelist.contains(extension.pkgName)
                }
            }

            val filteredNsfwExtensions = (_installed + _untrusted + _available)
                .any { it.isNsfw && !isExtensionAllowed(it) }

            val (updates, installed) = _installed
                .filter(isExtensionAllowed)
                .sortedWith(
                    compareBy<Extension.Installed> { !it.isObsolete }
                        .thenBy(String.CASE_INSENSITIVE_ORDER) { it.name },
                )
                .partition { it.hasUpdate }

            val untrusted = _untrusted
                .filter(isExtensionAllowed)
                .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })

            val available = _available
                .filter { extension ->
                    _installed.none { it.pkgName == extension.pkgName } &&
                        _untrusted.none { it.pkgName == extension.pkgName } &&
                        isExtensionAllowed(extension)
                }
                .flatMap { ext ->
                    ext.sources.filter { it.lang in enabledLanguages }
                        .map {
                            ext.copy(
                                name = it.name,
                                lang = it.lang,
                                pkgName = "${ext.pkgName}-${it.id}",
                                sources = listOf(it),
                            )
                        }
                }
                .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })

            Extensions(updates, installed, available, untrusted, filteredNsfwExtensions)
        }
    }
}
