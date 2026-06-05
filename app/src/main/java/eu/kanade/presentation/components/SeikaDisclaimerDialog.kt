package eu.kanade.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tachiyomi.presentation.core.components.material.padding

@Composable
fun SeikaDisclaimerDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AdaptiveSheet(
        onDismissRequest = onDismissRequest,
        enableImplicitDismiss = false,
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .weight(1f, fill = false)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Seika Disclaimer & Spiritual Reminder",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = "Seika is a fork of the manga reader Mihon, that is aimed at blocking all adult content, while keeping such extensions that allow reading them. The goal is to get the positive features of an extension instead of blocking it wholly for its negatives.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = "As a Muslim, this project is naturally targeted towards the Muslim audience, who also happen to enjoy JP/KR/CN/Western reading culture. It is not just a platform where they are protected from the harmful visuals, but also a reminder of some specific issues regarding those cultures:",
                    style = MaterialTheme.typography.bodyMedium,
                )

                DisclaimerSection(
                    title = "1. Immoral Content (Fawahish)",
                    content = "Comics containing nudity, explicit sexual scenes, or highly suggestive artwork fall under the broad prohibition of obscenity (Fahsha).\n" +
                        "• Quran: \"Say, 'My Lord has only forbidden immoralities (al-Fawahish)—what is apparent of them and what is concealed...'\" — Surah Al-A'raf, 7:33\n" +
                        "• Hadith: The Messenger of Allah (ﷺ) said, \"Verily, Allah does not love anyone vulgar and obscene.\" — Musnad Ahmad, 21764",
                )

                DisclaimerSection(
                    title = "2. Shirk (Polytheism) and Magic",
                    content = "Plots where characters worship idols, hold equal power to God, or portray witchcraft (sihr) positively violate foundational Islamic monotheism (Tawheed).\n" +
                        "• Quran: \"Indeed, Allah does not forgive associating others with Him in worship, but He forgives anything else of whoever He wills.\" — Surah An-Nisa, 4:48\n" +
                        "• Quran: \"...And the magician will not succeed wherever he goes.\" — Surah Ta-Ha, 20:69",
                )

                DisclaimerSection(
                    title = "3. Un-Islamic Values",
                    content = "Comics that normalize or mock Islamic ethics—such as glorifying alcohol, casual relationships, or rebellion against parents—directly contradict divine commands.\n" +
                        "• Quran: \"And do not cooperate in sin and aggression.\" — Surah Al-Ma'idah, 5:2\n" +
                        "• Hadith: The Prophet (ﷺ) said thrice, \"Shall I not inform you of the greatest of major sins?\" They said, \"Yes, O Messenger of Allah!\" He said, \"Comitting shirk and disobedience to parents.\" — Sahih al-Bukhari",
                )

                DisclaimerSection(
                    title = "4. Excessive Violence and Gore",
                    content = "While descriptions of battle can be permissible, the gratuitous glorification of cruelty, sadism, and senseless gore is forbidden because Islam emphasizes mercy.\n" +
                        "• Quran: \"...and do not commit abuse on the earth, spreading corruption.\" — Surah Al-Baqarah, 2:60\n" +
                        "• Hadith: The Messenger of Allah (ﷺ) said, \"Verily, Allah has prescribed excellence (ihsan) in all things. So when you kill, kill well; and when you slaughter, slaughter well...\" — Sahih Muslim, 1955",
                )

                DisclaimerSection(
                    title = "5. Neglecting Religious Duties",
                    content = "Any form of entertainment that consumes so much time that it causes you to delay or miss your daily mandatory prayers (Salah) becomes an impermissible distraction (Ghaflah).\n" +
                        "• Quran: \"So woe to those who pray, [but] who are heedless of their prayer.\" — Surah Al-Ma'un, 107:4-5\n" +
                        "• Hadith: The Prophet (ﷺ) said, \"There are two blessings that many people are deceived into losing: health and free time.\" — Sahih al-Bukhari, 6412",
                )

                DisclaimerSection(
                    title = "6. False Beliefs",
                    content = "Stories that treat false or blasphemous concepts—like reincarnation, human reincarnation as gods, or mocking the afterlife—as factual realities are forbidden to indulge in as truth.\n" +
                        "• Quran: \"And it has already come down to you in the Book that when you hear the verses of Allah denied [by them] and ridiculed; do not sit with them until they enter into another conversation...\" — Surah An-Nisa, 4:140",
                )

                Text(
                    text = "Hence, encouraging brothers of faith into slowly transitioning into a better path instead of tilting their faith into something worse.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier
                    .padding(MaterialTheme.padding.medium)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "I Understand / Acknowledge")
                }
            }
        }
    }
}

@Composable
private fun DisclaimerSection(
    title: String,
    content: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
