package org.teacon.xiaozhong;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod("xiaozhong")
@EventBusSubscriber
public class Xiaozhong {
    public Xiaozhong() {}

    @SubscribeEvent
    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        player.sendSystemMessage(Component.literal("Welcome to xiaozhong!"));
    }
}
