package flor.dsplugin

import arc.util.Log
import flor.dsplugin.CVars.logPrefix
import mindustry.mod.Plugin
import mindustry.net.Administration
import net.dv8tion.jda.api.utils.messages.MessageRequest

class DiscordPlugin : Plugin() {
    val token = Administration.Config("token", "Discord bot token.", "empty")
    val enabled = Administration.Config("enable-bot", "Enable/disable discord bot.", true)
    val multiServer = Administration.Config("multiserver", "Enable multi-server mode.", false);
    val serverChannel = Administration.Config("server-channel", "Server channel id.", "empty");
    val serverGuild = Administration.Config("server-guild", "Server guild id.", "empty");

    override fun init() {
        MessageRequest.setDefaultMentions(emptyList())

        val ts = token.string() // ts - token as string.
        if(ts == "empty" || ts.isNullOrBlank()) {
            Log.warn("$logPrefix Discord bot token is empty, skipping.")
        } else if(enabled.bool()) {
            Bot(token.string(), multiServer.bool(), serverChannel.string(), serverGuild.string())
        } else {
            Log.debug("$logPrefix Discord bot disabled, skipping.")
        }
    }
}