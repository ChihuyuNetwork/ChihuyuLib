package love.chihuyu.chihuyulib

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin

class ChihuyuLibPlugin: JavaPlugin() {
    companion object {
        lateinit var ChihuyuLibPlugin: JavaPlugin
    }

    init {
        ChihuyuLibPlugin = this
    }

    override fun onEnable() {
        super.onEnable()
        CommandAPI.onEnable()
    }

    override fun onDisable() {
        super.onDisable()
        CommandAPI.onDisable()
    }

    override fun onLoad() {
        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this)
                .silentLogs(false)
                .shouldHookPaperReload(true)
        )
    }
}