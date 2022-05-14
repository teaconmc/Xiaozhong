package org.teacon.xiaozhong;

import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("xiaozhong")
public class Xiaozhong {
    public Xiaozhong() {
        MinecraftForge.EVENT_BUS.addListener(PlayerLoggedInHandler::onLoggedIn);
    }

    public static class PlayerLoggedInHandler {
        public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            var player = event.getPlayer();
            player.sendMessage(new TextComponent("Welcome to xiaozhong!"), Util.NIL_UUID);
        }
    }
}
