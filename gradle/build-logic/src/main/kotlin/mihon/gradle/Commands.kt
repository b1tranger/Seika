package mihon.gradle

import org.gradle.api.Project
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

// Git is needed in your system PATH for these commands to work.
// If it's not installed, we return a default value as a workaround.
fun Project.getLatestCommitCount(): String {
    val count = exec("git rev-list --count HEAD")
    return if (count.isEmpty()) "1" else count
}

fun Project.getLatestCommitSha(): String {
    val sha = exec("git rev-parse --short HEAD")
    return if (sha.isEmpty()) "0000000" else sha
}

private val BUILD_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

/**
 * @param useLatestCommitTime If `true`, the build time is based on the timestamp of the last Git commit;
 *                          otherwise, the current time is used. Both are in UTC.
 * @return A formatted string representing the build time. The format used is defined by [BUILD_TIME_FORMATTER].
 */
fun Project.getBuildTime(useLatestCommitTime: Boolean): String {
    return if (useLatestCommitTime) {
        val result = exec("git log -1 --format=%ct")
        val epoch = result.toLongOrNull() ?: Instant.now().epochSecond
        Instant.ofEpochSecond(epoch).atOffset(ZoneOffset.UTC).format(BUILD_TIME_FORMATTER)
    } else {
        LocalDateTime.now(ZoneOffset.UTC).format(BUILD_TIME_FORMATTER)
    }
}

fun Project.exec(command: String): String {
    return try {
        providers.exec {
            commandLine = command.split(" ")
        }
            .standardOutput
            .asText
            .get()
            .trim()
    } catch (e: Exception) {
        ""
    }
}
