package org.teacon.xiaozhong;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
                () -> new Item(new Item.Properties()));
        SULFUR_BLOCK = BLOCKS.register(SULFUR_BLOCK_ID,
                () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2F, 1.5F)));
        SULFUR_BLOCK_ITEM = ITEMS.register(SULFUR_BLOCK_ID,
                () -> new BlockItem(SULFUR_BLOCK.get(), new Item.Properties()));
    }

    public Test1194() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Xiaozhong::registerCreativeModeTab);
    }

    public static void registerCreativeModeTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation("xiaozhong", "main_tab"), builder ->
                builder.displayItems((parameters, output) -> {
                    output.accept(SULFUR_DUST_ITEM.get());
                    output.accept(SULFUR_BLOCK_ITEM.get());
                }));
    }
}