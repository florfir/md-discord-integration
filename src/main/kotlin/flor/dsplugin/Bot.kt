package flor.dsplugin

import arc.Events
import arc.util.Log
import arc.util.Strings
import arc.util.Timer
import flor.dsplugin.CVars.logPrefix
import flor.dsplugin.utils.getPlayersCount
import flor.dsplugin.utils.stripFoo
import mindustry.Vars
import mindustry.game.EventType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.EnumSet

class Bot(botToken: String, val multiServer: Boolean, val serverChannelString: String, val serverGuildString: String) {
    val jda: JDA

    init {
        Log.info("$logPrefix Loading bot.")

        jda = JDABuilder
            .create(botToken, EnumSet.allOf(GatewayIntent::class.java))
            .build()

        jda.awaitReady()

        val guild = jda.getGuildById(serverGuildString)
        if(guild == null) {
            Log.err("$logPrefix Server guild not found!")
            // jda.shutdown()
        } else {
            val channel = guild.getTextChannelById(serverChannelString)
            if(channel != null) {
                jda.addEventListener(MessageListener(channel))

                Events.on(EventType.PlayerChatEvent::class.java) { e ->
                    val message = stripFoo(e.message)
                    val player = e.player

                    if(message.startsWith("/")) return@on

                    var content = "${player.plainName()}: ${Strings.stripColors(message)}"
                        .replace("\\", "\\\\")
                        .replace("`", "\\`")
                        .replace("@", "@\u200b")
                    content = "`$content`"

                    channel.sendMessage(content).queue()
                }
            } else {
                Log.err("$logPrefix Server channel not found!")
            }
        }

        if(!multiServer) {
            Timer.schedule({
                jda.presence.activity = Activity.playing("${getPlayersCount()} on ${Vars.state.map.plainName()}".take(127))
            }, 15f, 10f)
        }
    }
}