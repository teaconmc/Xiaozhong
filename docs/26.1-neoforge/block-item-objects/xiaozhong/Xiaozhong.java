package org.teacon.xiaozhong;

import com.google.common.collect.Iterables;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

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

    public static void onGatherData(GatherDataEvent.Client event) {
        event.createProvider(EnglishLanguageProvider::new);
        event.createProvider(ChineseLanguageProvider::new);
        event.createProvider(ModelAndBlockStateProvider::new);
        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(CustomBlockLoot::new, LootContextParamSets.EMPTY)),
                lookupProvider));
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

    public static class ModelAndBlockStateProvider extends ModelProvider {
        public ModelAndBlockStateProvider(PackOutput gen) {
            super(gen, "xiaozhong");
        }

        @Override
        protected void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {
            itemModelGenerators.generateFlatItem(SULFUR_DUST_ITEM.get(), ModelTemplates.FLAT_ITEM);

            // 因为我们的硫磺块六个面使用同一张贴图，我们仅需这一次调用就可以搞定 blockstate json 和 model json。
            blockModelGenerators.createTrivialCube(SULFUR_BLOCK.get());
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
