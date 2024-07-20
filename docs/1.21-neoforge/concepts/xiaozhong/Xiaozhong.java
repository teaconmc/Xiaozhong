package org.teacon.xiaozhong;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;


@Mod("xiaozhong")
public class Xiaozhong {
    public Xiaozhong(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(Xiaozhong::onLoggedIn);
        modEventBus.addListener(Xiaozhong::onGatherData);
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        player.sendSystemMessage(Component.translatable("chat.xiaozhong.welcome"));
    }

    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        gen.addProvider(event.includeClient(), new EnglishLanguageProvider(packOutput));
        gen.addProvider(event.includeClient(), new ChineseLanguageProvider(packOutput));
    }

    public static class EnglishLanguageProvider extends LanguageProvider {
        public EnglishLanguageProvider(PackOutput packOutput) {
            super(packOutput, "xiaozhong", "en_us");
        }

        @Override
        protected void addTranslations() {
            this.add("chat.xiaozhong.welcome", "Welcome to xiaozhong!");
        }
    }

    public static class ChineseLanguageProvider extends LanguageProvider {
        public ChineseLanguageProvider(PackOutput packOutput) {
            super(packOutput, "xiaozhong", "zh_cn");
        }

        @Override
        protected void addTranslations() {
            this.add("chat.xiaozhong.welcome", "欢迎来到正山小种！");
        }
    }
}
