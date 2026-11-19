package flor.dsplugin.utils

import arc.Core
import mindustry.gen.Groups

fun stripFoo(string: String): String {
    val var1 = StringBuilder(string)
    for (i in string.length - 1 downTo 0) {
        if (var1[i].code in 0xf80..0x107f) var1.deleteCharAt(i)
    }
    return var1.toString()
}

fun getPlayersCount(): Int = if (Core.settings.getInt("totalPlayers") == 0) Groups.player.size() else Core.settings.getInt("totalPlayers")