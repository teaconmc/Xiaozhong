package org.teacon.xiaozhong;

import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod("xiaozhong")
public class Xiaozhong {
    public Xiaozhong() {
        MinecraftForge.EVENT_BUS.addListener(Xiaozhong::onLoggedIn);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Xiaozhong::onGatherData);
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getPlayer();
        player.sendMessage(new TranslatableComponent("chat.xiaozhong.welcome"), Util.NIL_UUID);
    }

    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        gen.addProvider(new EnglishLanguageProvider(gen));
        gen.addProvider(new ChineseLanguageProvider(gen));
    }

    public static class EnglishLanguageProvider extends LanguageProvider {
        public EnglishLanguageProvider(DataGenerator gen) {
            super(gen, "xiaozhong", "en_us");
        }

        @Override
        protected void addTranslations() {
            this.add("chat.xiaozhong.welcome", "Welcome to xiaozhong!");
        }
    }

    public static class ChineseLanguageProvider extends LanguageProvider {
        public ChineseLanguageProvider(DataGenerator gen) {
            super(gen, "xiaozhong", "zh_cn");
        }

        @Override
        protected void addTranslations() {
            this.add("chat.xiaozhong.welcome", "欢迎来到正山小种！");
        }
    }
}
