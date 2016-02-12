package com.laytonsmith.commandhelper;

import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 *
 */
public class CommandHelperInterpreterListener implements Listener {

    private final CommandHelperCommon common;

    public CommandHelperInterpreterListener(CommandHelperCommon common) {
        this.common = common;
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        if (common.isInInterpreterMode(event.getPlayer().getName())) {
            final MCPlayer p = new BukkitMCPlayer(event.getPlayer());
            event.setCancelled(true);
            StaticLayer.SetFutureRunnable(null, 0, new Runnable() {

                @Override
                public void run() {
                    common.textLine(p, event.getMessage());
                }
            });
        }

    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        common.stopInterpret(event.getPlayer().getName());
        common.multilineMode.remove(event.getPlayer().getName());
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()){
			return;
		}
        if (common.isInInterpreterMode(event.getPlayer().getName())) {
            MCPlayer p = new BukkitMCPlayer(event.getPlayer());
            common.textLine(p, event.getMessage());
            event.setCancelled(true);
        }
    }
}
