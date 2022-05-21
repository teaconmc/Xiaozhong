package org.teacon.xiaozhong;

import com.mojang.datafixers.DSL;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

@Mod("xiaozhong")
public class Xiaozhong {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "xiaozhong");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, "xiaozhong");

    public static final RegistryObject<Block> MY_MACHINE;
    public static final RegistryObject<BlockEntityType<MyMachineEntity>> MY_MACHINE_BLOCK_ENTITY;

    static {
        MY_MACHINE = BLOCKS.register("my_machine",
                () -> new MyMachine(BlockBehaviour.Properties.of(Material.METAL)));
        MY_MACHINE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("my_machine",
                () -> BlockEntityType.Builder.of(MyMachineEntity::new, MY_MACHINE.get()).build(DSL.remainderType()));
    }

    public Xiaozhong() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final class MyMachine extends BaseEntityBlock {
        public MyMachine(Properties props) {
            super(props);
        }

        @Nonnull
        @Override
        public RenderShape getRenderShape(@Nonnull BlockState state) {
            return RenderShape.MODEL;
        }

        @Override
        public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new MyMachineEntity(pos, state);
        }

        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
            return level.isClientSide() ? null : createTickerHelper(blockEntityType, MY_MACHINE_BLOCK_ENTITY.get(), MyMachineEntity::tick);
        }
    }

    public static final class MyMachineEntity extends BlockEntity {
        public MyMachineEntity(BlockEntityType<MyMachineEntity> type, BlockPos worldPosition, BlockState blockState) {
            super(type, worldPosition, blockState);
        }

        public MyMachineEntity(BlockPos worldPosition, BlockState blockState) {
            this(MY_MACHINE_BLOCK_ENTITY.get(), worldPosition, blockState);
        }

        private int count = 0;

        public static void tick(Level level, BlockPos pos, BlockState state, MyMachineEntity entity) {
            entity.count += 1;
            if (entity.count > 100) {
                entity.count = 0;
                if (level != null && !level.isClientSide()) {
                    var player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5.0, false);
                    if (player != null) {
                        player.sendMessage(new TranslatableComponent("chat.xiaozhong.welcome"), Util.NIL_UUID);
                    }
                }
            }
        }
    }
}
