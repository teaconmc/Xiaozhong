package org.teacon.xiaozhong;

import com.google.common.collect.Iterables;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod("xiaozhong")
public class Xiaozhong {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "xiaozhong");
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "xiaozhong");

    public static final String SULFUR_DUST_ID = "sulfur_dust";
    public static final RegistryObject<Item> SULFUR_DUST_ITEM;

    public static final String SULFUR_BLOCK_ID = "sulfur_block";
    public static final RegistryObject<Block> SULFUR_BLOCK;
    public static final RegistryObject<BlockItem> SULFUR_BLOCK_ITEM;

    static {
        SULFUR_DUST_ITEM = ITEMS.register(SULFUR_DUST_ID,
                () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        SULFUR_BLOCK = BLOCKS.register(SULFUR_BLOCK_ID,
                () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2F, 1.5F)));
        SULFUR_BLOCK_ITEM = ITEMS.register(SULFUR_BLOCK_ID,
                () -> new BlockItem(SULFUR_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
    }

    public Xiaozhong() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Xiaozhong::onGatherData);
    }

    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var helper = event.getExistingFileHelper();
        gen.addProvider(new EnglishLanguageProvider(gen));
        gen.addProvider(new ChineseLanguageProvider(gen));
        gen.addProvider(new ModelProvider(gen, helper));
        gen.addProvider(new StateProvider(gen, helper));
        gen.addProvider(new LootProvider(gen));
    }

    public static class EnglishLanguageProvider extends LanguageProvider {
        public EnglishLanguageProvider(DataGenerator gen) {
            super(gen, "xiaozhong", "en_us");
        }

        @Override
        protected void addTranslations() {
            this.add(SULFUR_DUST_ITEM.get(), "Sulfur Dust");
            this.add(SULFUR_BLOCK.get(), "Sulfur Block");
        }
    }

    public static class ChineseLanguageProvider extends LanguageProvider {
        public ChineseLanguageProvider(DataGenerator gen) {
            super(gen, "xiaozhong", "zh_cn");
        }

        @Override
        protected void addTranslations() {
            this.add(SULFUR_DUST_ITEM.get(), "硫粉");
            this.add(SULFUR_BLOCK.get(), "硫磺块");
        }
    }

    public static class ModelProvider extends ItemModelProvider {
        public ModelProvider(DataGenerator gen, ExistingFileHelper helper) {
            super(gen, "xiaozhong", helper);
        }

        @Override
        protected void registerModels() {
            this.singleTexture(SULFUR_DUST_ID, new ResourceLocation("item/generated"), "layer0", new ResourceLocation("xiaozhong", "item/" + SULFUR_DUST_ID));
        }
    }

    public static class StateProvider extends BlockStateProvider {
        public StateProvider(DataGenerator gen, ExistingFileHelper helper) {
            super(gen, "xiaozhong", helper);
        }

        @Override
        protected void registerStatesAndModels() {
            this.simpleBlock(SULFUR_BLOCK.get(), this.cubeAll(SULFUR_BLOCK.get()));
            this.simpleBlockItem(SULFUR_BLOCK.get(), this.cubeAll(SULFUR_BLOCK.get()));
        }
    }

    public static class LootProvider extends LootTableProvider {
        public LootProvider(DataGenerator gen) {
            super(gen);
        }

        @Nonnull
        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return List.of(Pair.of(CustomBlockLoot::new, LootContextParamSets.BLOCK));
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext context) {
            map.forEach((key, value) -> LootTables.validate(context, key, value));
        }
    }

    public static class CustomBlockLoot extends BlockLoot {
        @Override
        protected void addTables() {
            this.dropSelf(SULFUR_BLOCK.get());
        }

        @Nonnull
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Iterables.transform(BLOCKS.getEntries(), RegistryObject::get);
        }
    }
}
