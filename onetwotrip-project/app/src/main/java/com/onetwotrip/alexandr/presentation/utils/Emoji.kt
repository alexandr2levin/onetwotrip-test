package org.flycraft.tonguetwisterslibrary.android.presentation.utils

import android.text.SpannableStringBuilder
import android.text.Spanned

object Emoji {

    private val aliasToEmojies = mapOf(
            ":wink:" to "😉",
            ":+1:" to "👍",
            ":wave:" to "👋",
            ":stuck_out_tongue_winking_eye:" to "😜",
            ":thinking:" to "🤔",
            ":sob:" to "😭",
            ":sweat:" to "😓",
            ":scream:" to "😱",
            ":sweat_smile:" to "😅",
            ":smiley:" to "😃",
            ":thought_balloon:" to "💭",
            ":pray:" to "🙏",
            ":books:" to "📚",
            ":scroll:" to "📜",
            ":nerd:" to "🤓",
            ":student_girl:" to "👩‍🎓"
    )

    fun replaceAliasWithEmoji(text: String): String {
        var finalText = text
        aliasToEmojies.forEach { (alias, emoji) ->
            finalText = finalText.replace(alias, emoji)
        }
        return finalText
    }

    fun replaceAliasWithEmoji(text: Spanned): Spanned {
        val spannableStringBuilder = SpannableStringBuilder(text)

        aliasToEmojies.forEach { (alias, emoji) ->
            var startIndexToReplace = spannableStringBuilder.indexOf(alias)
            while(startIndexToReplace > 0) {
                val endIndex = startIndexToReplace + alias.length
                spannableStringBuilder.replace(startIndexToReplace, endIndex, emoji)
                startIndexToReplace = spannableStringBuilder.indexOf(alias)
            }
        }

        return spannableStringBuilder
    }

}