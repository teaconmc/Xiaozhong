package org.teacon.xiaozhong;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod("xiaozhong")
public class Xiaozhong {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.Items.createItems("xiaozhong");
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.Blocks.createBlocks("xiaozhong");

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
    }
}
