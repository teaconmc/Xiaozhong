package org.teacon.xiaozhong;

import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
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

    public static Holder<ConfiguredFeature<?, ?>> GOLDEN_TREE_FEATURE;

    public static Holder<PlacedFeature> GOLDEN_TREE;

    public Xiaozhong() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, FMLCommonSetupEvent.class, Xiaozhong::setup);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, BiomeLoadingEvent.class, Xiaozhong::biomeLoading);
    }

    public static void registerFeatures() {
        GOLDEN_TREE_FEATURE = FeatureUtils.register(
                "xiaozhong:golden_tree",
                Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(Blocks.GOLD_BLOCK),
                        new StraightTrunkPlacer(8, 12, 0),
                        BlockStateProvider.simple(Blocks.GLOWSTONE),
                        new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), 4),
                        new TwoLayersFeatureSize(1,1,1)
                ).build()
        );
        GOLDEN_TREE = PlacementUtils.register(
                "xiaozhong:golden_tree",
                GOLDEN_TREE_FEATURE,
                PlacementUtils.countExtra(6, 0.1F, 1),
                InSquarePlacement.spread(),
                VegetationPlacements.TREE_THRESHOLD,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                BiomeFilter.biome()
        );
    }

    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(Xiaozhong::registerFeatures);
    }

    public static void biomeLoading(BiomeLoadingEvent event) {
        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GOLDEN_TREE);
    }

}