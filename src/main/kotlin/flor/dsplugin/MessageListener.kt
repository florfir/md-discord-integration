package flor.dsplugin

import arc.graphics.Color
import arc.util.Log
import mindustry.gen.Call
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.text.MessageFormat

class MessageListener(val serverChannel: TextChannel) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromGuild) return
        val author = event.author
        val message = event.message
        if (author.isBot || author.isSystem || message.isWebhookMessage) return
        val member = event.member

        val msgChannel = event.getChannel()
        val content = message.contentDisplay

        if(member != null && msgChannel.id == serverChannel.id) {
            val username = member.effectiveName

            Log.info("@: @", username, content)

            val colorHex: String = Color(member.colors.primaryRaw).toString()
            val mindustryMessage =
                MessageFormat
                    .format(
                        "[blue]\uE80D[tan][[[#{0}]{1}[tan]][white]: {2}",
                        colorHex,
                        username,
                        content,
                    ).take(250)

            Call.sendMessage(mindustryMessage)
        }
    }
}