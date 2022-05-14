# 方块实体

Minecraft 使用名为方块实体（Block Entity）的机制来实现一些一般情况下方块做不到或很难做到的事情，例如

  - 持有更复杂的数据（物品、文本等）
  - 实现像熔炉那样持续不断地行为
  - 拥有奇妙的渲染特效

## `BlockEntity` 和 `BlockEntityType<?>`

Minecraft 中所有的方块实体均属于 `BlockEntity` 类型。我们首先需要创建我们自己的 `BlockEntity` 类。

```java
public static final class MyMachineEntity extends BlockEntity {
    public MyMachineEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }
}
```

然后我们需要注册一个 `BlockEntityType` 实例，它代表了「一种特定的方块实体」，指明了创建对应方块实体实例的工厂方法，及哪些方块允许持有这种方块实体。
`BlockEntityType` 的注册和方块及物品一样，由 Forge 托管，对应的注册系统为 `ForgeRegistries.BLOCK_ENTITIES`。
使用 `DeferredRegister` 注册 `BlockEntityType`：

```java
// 注册 BlockEntityType
public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, "xiaozhong");

public static final RegistryObject<BlockEntityType<MyMachienEntity>> MY_MACHINE_ENTITY = BLOCK_ENTITIES.register("my_machine", () -> BlockEntityType.Builder.of(MyMachineEntity::new, MY_MACHINE.get()).build(DSL.remainderType()));
```

注意到 `MyMachineEntity::new` 此时会报错。刚才提到的「方块实体实例的工厂方法」就是这个，然而原版要求「这个工厂方法只接受两个参数：`BlockPos` 和 `BlockState`」。我们对 `MyMachineEntity` 的构造器进行如下修改来解决此问题：

```java
public MyMachineEntity(BlockPos worldPosition, BlockState blockState) {
    super(MY_MACHINE_ENTITY.get(), worldPosition, blockState);
}
```

:::info

这个案例也展示了 `DeferredRegistry` 的一个优势：可以方便开发者打破这种出现循环依赖的情况。

:::

`BlockEntity` 此时像是一个纯粹的数据容器：我们在我们的 `MyMachineEntity` 中可以随意添加新的成员字段，并视情况决定是否创建对应的访问器。

```java
public static final class MyMachineEntity extends BlockEntity {
    public MyMachineEntity(BlockPos worldPosition, BlockState blockState) {
        super(MY_MACHINE_BLOCK_ENTITY.get(), worldPosition, blockState);
    }

    private int count = 0;
}
```

## `BlockEntityTicker<T extends BlockEntity>`

默认情况下，方块实体并不具备跟随游戏刻刷新的能力，若要获得此能力，方块实体所在的那个方块需要明确声明一个所谓的「Ticker」。

此外，你可以根据 `Level` 是在逻辑服务器上还是逻辑客户端上来返回不同的 Ticker。通常我们在客户端不需要特别的逻辑，因此我们会在 `level.isClientSide()` 返回 `true` 时，令 `getTicker` 返回 `null`。

首先在 `MyMachineEntity` 中创建静态方法 `tick`：

```java
public static void tick(Level level, BlockPos pos, BlockState state, MyMachineEntity entity) {
    // 计数。每一个游戏刻中，游戏都会调用一次 tick 方法，所以我们只需要在这里让 count 自增 1 即可。
    ++entity.count;
    // 当计数超过 100 时……
    if (entity.count > 100) {
        // 首先重置计数
        entity.count = 0;
        // 检查 Level 实例是否存在，以及是不是在服务器上
        if (level != null && !level.isClientSide()) {
            // 然后寻找半径 5 方块以内的玩家，最后的 false 表示无视创造或观察模式的玩家
            var p = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5.0, false);
            // 如果找到了这样的玩家……
            if (p != null) {
                // 向这名玩家的聊天栏发送问候语。
                p.sendMessage(new TranslatableComponent("xiaozhong.greeting"), Util.NIL_UUID);
            }
        }
    }
}
```

然后在我们的方块类中，覆写 `getTicker` 方法：

```java
@Nullable
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return level.isClientSide() ? null : createTickerHelper(blockEntityType, MY_MACHINE_BLOCK_ENTITY.get(), MyMachineEntity::tick);
}
```

以下演示了一个最基础方块实体的实现及的注册流程——没有相应的语言文件，没有模型，什么都没有，甚至没有为该方块注册对应的物品：

```java
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod("xiaozhong")
public class MachineMod {

    public static final class MyMachine extends BaseEntityBlock {

        public MyMachine(Properties props) {
            super(props);
        }

        @NotNull
        @Override
        public RenderShape getRenderShape(BlockState state) {
            return RenderShape.MODEL;
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new MyMachineEntity(pos, state);
        }

        @Nullable
        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
            return level.isClientSide() ? null : createTickerHelper(blockEntityType, MY_MACHINE_BLOCK_ENTITY.get(), MyMachineEntity::tick);
        }
    }

    public static final class MyMachineEntity extends BlockEntity {
        public MyMachineEntity(BlockPos pWorldPosition, BlockState pBlockState) {
            super(MY_MACHINE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        }

        private int count = 0;

        public static void tick(Level level, BlockPos pos, BlockState state, MyMachineEntity entity) {
            ++entity.count;
            if (entity.count > 100) {
                entity.count = 0;
                if (level != null && !level.isClientSide()) {
                    var p = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5.0, false);
                    if (p != null) {
                        p.sendMessage(new TranslatableComponent("xiaozhong.greeting"), Util.NIL_UUID);
                    }
                }
            }
        }
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "xiaozhong");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, "xiaozhong");

    public static final RegistryObject<Block> MY_MACHINE = BLOCKS.register("my_machine",
            () -> new MyMachine(BlockBehaviour.Properties.of(Material.METAL)));
    public static final RegistryObject<BlockEntityType<MyMachineEntity>> MY_MACHINE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("my_machine",
            () -> BlockEntityType.Builder.of(MyMachineEntity::new, MY_MACHINE.get()).build(DSL.remainderType()));

    public MachineMod() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
```

这个方块实体的效果是，每 100 个游戏刻检查是否有玩家距离该方块实体距离不到 5 方块，若有，随机抽一位向其打招呼。

打开游戏，执行 `/setblock ~ ~-1 ~ xiaozhong:my_machine` 看看效果吧～