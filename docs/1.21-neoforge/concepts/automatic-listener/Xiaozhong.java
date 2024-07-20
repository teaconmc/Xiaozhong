package org.teacon.xiaozhong;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod("xiaozhong")
public class Xiaozhong {
    public Xiaozhong() {}

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
    public static class PlayerLoggedInHandler {
        @SubscribeEvent
        public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            var player = event.getEntity();
            player.sendSystemMessage(Component.literal("Welcome to xiaozhong!"));
        }
    }
}
