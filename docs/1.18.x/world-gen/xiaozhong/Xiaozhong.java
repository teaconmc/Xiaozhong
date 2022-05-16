package org.teacon.xiaozhong;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

@Mod("xiaozhong")
public class Xiaozhong {
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> MY_ORE_FEATURE;
    public static Holder<PlacedFeature> MY_ORE;

    public Xiaozhong() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Xiaozhong::setup);
        MinecraftForge.EVENT_BUS.addListener(Xiaozhong::biomeLoading);
    }

    public static void registerFeatures() {
        MY_ORE_FEATURE = FeatureUtils.register(
                "xiaozhong:underground_ice",
                Feature.ORE,
                new OreConfiguration(
                        OreFeatures.STONE_ORE_REPLACEABLES,
                        Blocks.BLUE_ICE.defaultBlockState(), 60));
        MY_ORE = PlacementUtils.register(
                "xiaozhong:underground_ice",
                MY_ORE_FEATURE,
                List.of(CountPlacement.of(30),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.bottom(),
                                VerticalAnchor.top())));
    }

    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(Xiaozhong::registerFeatures);
    }

    public static void biomeLoading(BiomeLoadingEvent event) {
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MY_ORE);
    }
}
