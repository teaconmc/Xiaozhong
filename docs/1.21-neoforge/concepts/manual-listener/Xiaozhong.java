package org.teacon.xiaozhong;

import net.minecraft.network.chat.Component;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod("xiaozhong")
public class Xiaozhong {
    public Xiaozhong() {
        NeoForge.EVENT_BUS.addListener(PlayerLoggedInHandler::onLoggedIn);
    }

    public static class PlayerLoggedInHandler {
        public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            var player = event.getEntity();
            player.sendSystemMessage(Component.literal("Welcome to xiaozhong!"));
        }
    }
}
