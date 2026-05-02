package org.teacon.xiaozhong;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod("xiaozhong")
public class Xiaozhong {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.Items.createItems("xiaozhong");
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.Blocks.createBlocks("xiaozhong");
    public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "xiaozhong");

    public static final String SULFUR_DUST_ID = "sulfur_dust";
    public static final DeferredHolder<Item, Item> SULFUR_DUST_ITEM;

    public static final String SULFUR_BLOCK_ID = "sulfur_block";
    public static final DeferredHolder<Block, Block> SULFUR_BLOCK;
    public static final DeferredHolder<Item, BlockItem> SULFUR_BLOCK_ITEM;

    public static final String MAIN_TAB_ID = "main_tab";
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB;

    static {
        SULFUR_DUST_ITEM = ITEMS.register(SULFUR_DUST_ID,
                () -> new Item(new Item.Properties()));
        SULFUR_BLOCK = BLOCKS.register(SULFUR_BLOCK_ID,
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops().strength(2F, 1.5F)));
        SULFUR_BLOCK_ITEM = ITEMS.register(SULFUR_BLOCK_ID,
                () -> new BlockItem(SULFUR_BLOCK.get(), new Item.Properties()));
        MAIN_TAB = TABS.register(MAIN_TAB_ID,
                () -> CreativeModeTab.builder().icon(() -> new ItemStack(SULFUR_DUST_ITEM)).build());
    }

    public Xiaozhong(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        TABS.register(modEventBus);
        modEventBus.addListener(Xiaozhong::buildCreativeTabContent);
    }

    public static void buildCreativeTabContent(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == MAIN_TAB.get()) {
            event.accept(SULFUR_DUST_ITEM.get());
            event.accept(SULFUR_BLOCK_ITEM.get());
        }
    }
}