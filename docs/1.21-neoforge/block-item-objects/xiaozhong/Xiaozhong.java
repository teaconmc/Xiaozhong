package org.teacon.xiaozhong;

import com.google.common.collect.Iterables;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod("xiaozhong")
public class Xiaozhong {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.Items.createItems("xiaozhong");
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.Blocks.createBlocks("xiaozhong");

    public static final String SULFUR_DUST_ID = "sulfur_dust";
    public static final DeferredHolder<Item, Item> SULFUR_DUST_ITEM;

    public static final String SULFUR_BLOCK_ID = "sulfur_block";
    public static final DeferredHolder<Block, Block> SULFUR_BLOCK;
    public static final DeferredHolder<Item, BlockItem> SULFUR_BLOCK_ITEM;

    static {
        SULFUR_DUST_ITEM = ITEMS.register(SULFUR_DUST_ID,
                () -> new Item(new Item.Properties()));
        SULFUR_BLOCK = BLOCKS.register(SULFUR_BLOCK_ID,
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops().strength(2F, 1.5F)));
        SULFUR_BLOCK_ITEM = ITEMS.register(SULFUR_BLOCK_ID,
                () -> new BlockItem(SULFUR_BLOCK.get(), new Item.Properties()));
    }

    public Xiaozhong(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        modEventBus.addListener(Xiaozhong::onGatherData);
    }

    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        var helper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();
        gen.addProvider(event.includeClient(), new EnglishLanguageProvider(packOutput));
        gen.addProvider(event.includeClient(), new ChineseLanguageProvider(packOutput));
        gen.addProvider(event.includeClient(), new ModelProvider(packOutput, helper));
        gen.addProvider(event.includeClient(), new StateProvider(packOutput, helper));
        gen.addProvider(event.includeServer(), new LootProvider(packOutput, lookupProvider));
    }

    public static class EnglishLanguageProvider extends LanguageProvider {
        public EnglishLanguageProvider(PackOutput gen) {
            super(gen, "xiaozhong", "en_us");
        }

        @Override
        protected void addTranslations() {
            this.add(SULFUR_DUST_ITEM.get(), "Sulfur Dust");
            this.add(SULFUR_BLOCK.get(), "Sulfur Block");
        }
    }

    public static class ChineseLanguageProvider extends LanguageProvider {
        public ChineseLanguageProvider(PackOutput gen) {
            super(gen, "xiaozhong", "zh_cn");
        }

        @Override
        protected void addTranslations() {
            this.add(SULFUR_DUST_ITEM.get(), "硫粉");
            this.add(SULFUR_BLOCK.get(), "硫磺块");
        }
    }

    public static class ModelProvider extends ItemModelProvider {
        public ModelProvider(PackOutput gen, ExistingFileHelper helper) {
            super(gen, "xiaozhong", helper);
        }

        @Override
        protected void registerModels() {
            this.singleTexture(SULFUR_DUST_ID, ResourceLocation.withDefaultNamespace("item/generated"), "layer0", ResourceLocation.fromNamespaceAndPath("xiaozhong", "item/" + SULFUR_DUST_ID));
        }
    }

    public static class StateProvider extends BlockStateProvider {
        public StateProvider(PackOutput gen, ExistingFileHelper helper) {
            super(gen, "xiaozhong", helper);
        }

        @Override
        protected void registerStatesAndModels() {
            this.simpleBlock(SULFUR_BLOCK.get(), this.cubeAll(SULFUR_BLOCK.get()));
            this.simpleBlockItem(SULFUR_BLOCK.get(), this.cubeAll(SULFUR_BLOCK.get()));
        }
    }

    public static class LootProvider extends LootTableProvider {
        public LootProvider(PackOutput gen, CompletableFuture<HolderLookup.Provider> lookup) {
            super(gen, Set.of(), List.of(new SubProviderEntry(CustomBlockLoot::new, LootContextParamSets.BLOCK)), lookup);
        }

        @Override
        protected void validate(WritableRegistry<LootTable> registry, ValidationContext context, ProblemReporter.Collector collector) {
            // FIXME Need proper migration
            // map.forEach((key, value) -> LootTables.validate(context, key, value));
        }
    }

    public static class CustomBlockLoot extends BlockLootSubProvider {
        protected CustomBlockLoot(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        @Override
        protected void generate() {
            this.dropSelf(SULFUR_BLOCK.get());
        }

        @NotNull
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Iterables.transform(BLOCKS.getEntries(), DeferredHolder::get);
        }
    }
}
