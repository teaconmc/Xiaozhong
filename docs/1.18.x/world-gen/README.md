# 世界生成

Minecraft 将世界生成划分为了若干个阶段，其中模组开发者们经常关注的阶段有下列几个：

  - 生物群系生成：游戏会在这一阶段中决定世界的每一个位置上都是什么生物群系。
  - 地形生成：游戏会在这一阶段中决定世界的整体地势地貌。
  - 地表生成：游戏会在这一阶段中根据生物群系等信息，为光秃秃的地表覆盖上一层「衣服」，例如沙漠的沙子、平原的草方块等
  - 大型结构生成：游戏会在这一阶段中随机放置少量大型建筑（村庄、要塞、神殿、等）
  - 地物生成：游戏会在这一阶段中随机放置小型的地表、地下装饰，如树木、竹子、大型蘑菇、晶洞、各类矿石等。

我们接下来将通过几个实例来说明「什么时候该用什么」。

## 生成矿物

矿物是一种地物（Feature）。原版准备了名为 `OreFeature` 的类来负责所有「矿物」的生成，这包括：

  - 各种矿物（煤、铁、金、红石、钻石、青金石、绿宝石、石英、铜）
  - 地下的泥土、沙砾、花岗岩、闪长岩、安山岩沉积
  - 地狱的岩浆块堆
  - ……

不同的矿物需要不同的配置（生成哪种方块、替换哪种方块、生成多少），因此我们还需要一个和 `OreFeature` 配套的 `OreConfiguration` 来对 `OreFeature` 进行配置。

```java
public static Holder<ConfiguredFeature<OreConfiguration, ?>> MY_ORE_FEATURE;

public static void register() {
  MY_ORE_FEATURE = BuiltinRegistries.register(
      BuiltinRegistries.CONFIGURED_FEATURE, // 我们要向这个注册表注册我们的生成器。
      new ResourceLocation("xiaozhong", "underground_ice"), // 我们这个生成器的名字是 xiaozhong:underground_ice。
      new ConfiguredFeature<>(Feature.ORE, new OreConfiguration( // 这个生成器基于 Feature.ORE，配置如下：
        OreFeatures.STONE_ORE_REPLACEABLES,  // 这个生成器将原版的「石头」……
        Blocks.BLUE_ICE.defaultBlockState(), // ……替换为原版的蓝冰，默认状态，
        60                                   // 一批最多 60 块。
      )));
}
```

然后，我们需要为这个生成器与一套「放置规则」搭配，形成一个所谓的 `PlacedFeature`，并注册：

```java
public static Holder<ConfiguredFeature<?, ?>> MY_ORE_FEATURE;

public static Holder<PlacedFeature> MY_ORE;

public static void registerFeatures() {
  MY_ORE_FEATURE = BuiltinRegistries.register(
      BuiltinRegistries.CONFIGURED_FEATURE, // 我们要向这个注册表注册我们的生成器。
      new ResourceLocation("xiaozhong", "underground_ice"), // 我们这个生成器的名字是 xiaozhong:underground_ice。
      new ConfiguredFeature<>(Feature.ORE, new OreConfiguration( // 这个生成器基于 Feature.ORE，配置如下：
        OreFeatures.STONE_ORE_REPLACEABLES,  // 这个生成器将原版的「石头」……
        Blocks.BLUE_ICE.defaultBlockState(), // ……替换为原版的蓝冰，默认状态，
        60                                   // 一批最多 60 块。
      )));
  MY_ORE = BuiltinRegistries.register(
      BuiltinRegistries.PLACED_FEATURE, // 我们要向这个注册表注册我们的 PlacedFeature。
          new ResourceLocation("xiaozhong", "underground_ice"), // 它的名字是 xiaozhong:underground_ice。
          new PlacedFeature(MY_ORE_FEATURE, // 这个 PlacedFeature 使用我们刚刚注册的 MY_ORE_FEATURE
            List.of(                        // 使用这些放置规则：
              CountPlacement.of(30),        // 一个区块内尝试 30 次，
              InSquarePlacement.spread(),   // X 和 Z 方向上有 0 - 15 格不等的随机漂移，
              HeightRangePlacement.uniform( // 高度限制在……
                VerticalAnchor.bottom(),    // ……从世界最低点……
                VerticalAnchor.top()))));   // ……到世界最高点。
}
```

然后我们订阅 `FMLCommonSetupEvent`，在其中__推迟__调用此方法：

```java
@SubscribeEvent
public static void setup(FMLCommonSetupEvent event) {
  // WorldGen 为 registerFeatures 所在的类的类名，请按需替换。
  event.enqueueWork(WorldGen::registerFeatures);
}
```

最后，我们要将 `PlacedFeature` 加入世界生成。
原版的每一个生物群系都对应一套生物群系内特有的生成规则，其中包括「待使用的 `PlacedFeature` 列表」，那么我们的目标就是要把我们的 `MY_ORE` 塞进所有生物群系的这个表里。

订阅 `BiomeLoadingEvent`：

```java
@SubscribeEvent
public static void onBiomeLoading(BiomeLoadingEvent event) {
  // 向当前正在加载的生物群系生成规则中，添加：在生成地下矿石这一步时，使用我们的 MY_ORE
  // 因为每一个生物群系加载时都会过一遍此事件，因此这样做相当于向所有生物群系添加此生成器。
  event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.MY_ORE);
}
```

:::caution

`BiomeLoadingEvent` 发布在 `MinecraftForge.FORGE` 事件总线上，请在订阅此事件时多加留意，不要选错事件发布总线。

:::

以下演示了上述整个注册矿物生成的流程：

```java
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
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
public class WorldGen {

    public WorldGen() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, FMLCommonSetupEvent.class, WorldGen::setup);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, BiomeLoadingEvent.class, WorldGen::biomeLoading);
    }

    public static Holder<ConfiguredFeature<?, ?>> MY_ORE_FEATURE;

    public static Holder<PlacedFeature> MY_ORE;

    public static void registerFeatures() {
        MY_ORE_FEATURE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new ResourceLocation("xiaozhong", "underground_ice"),
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                        OreFeatures.STONE_ORE_REPLACEABLES, Blocks.BLUE_ICE.defaultBlockState(), 60
                )));
        MY_ORE = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE,
                new ResourceLocation("xiaozhong", "underground_ice"),
                new PlacedFeature(MY_ORE_FEATURE, List.of(CountPlacement.of(30), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))));
    }

    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(WorldGen::registerFeatures);
    }

    public static void biomeLoading(BiomeLoadingEvent event) {
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.newOres);
    }

}
```


若操作正确，打开游戏后便可轻易找到漫山遍野的「冰块矿」了。

![冰块矿](./ice-ore.png)

## 生成野生植物

花也是一种地物。原版使用 `RandomPatchFeature` 来实现平原、向日葵平原、森林、繁花森林中的野花生成。

使用同样的 `RandomPatchFeature` 我们可以轻松实现各种野生作物的生成。


事实上，`RandomPatchFeature` 这个名字直译过来是「随机小片地物」，因此 `RandomPatchFeature` 并不局限于生成「野生植物」，例如原版下界中的「野火」也是基于 `RandomPatchFeature` 的。

## 生成树木

树也是一种地物，但树的生成较为复杂，因为原版将树的生成切割成了三部分：

  1. 树干/树桩。
  2. 树冠/树叶。
  3. 树上的装饰，例如丛林及沼泽中树上的藤蔓，森林中树上的蜂巢等）。

这些属性共同组成了 `TreeConfiguration`，用于和 `TreeFeature` 搭配使用。

原版为我们提供了 `TreeConfiguration.TreeConfigurationBuilder` 以方便我们配置全新树木地物。