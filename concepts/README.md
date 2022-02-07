# 基本概念

以下是本篇指南所用到的基本概念。对部分 Minecraft 模组玩家和资源包 / 数据包创作者来说，一些概念可能已经相对熟悉了。

## 映射表

Minecraft 官方未提供源代码，因此开发模组所看到的「Minecraft 源代码」均由一个名叫 ForgeGradle 的工具内部处理完成。Forge MDK 默认引用了这一工具。为使 ForgeGradle 正确处理字段名、方法名、类名、以及包名，`build.gradle` 内部指定了处理「Minecraft 源代码」所需要的映射表。相关配置位于 `minecraft {}` 块内：

```groovy
minecraft {
    mappings channel: 'official', version: '1.17.1'
    runs { /* ... */ }
}
```

?> Forge MDK 默认指定的映射表未包含变量名的相关数据，而一个名叫「Parchment」的项目正好填补了这一空白。如欲使用「Parchment」映射表，请参阅 [Parchment 官方文档](https://github.com/ParchmentMC/Librarian/blob/dev/docs/FORGEGRADLE.md)。

## 源代码

Forge MDK 默认从 `src/main/java` 检索 Java 源代码，将其编译并进行适当处理后封装到最终的模组文件中。

!> 源代码通常使用 UTF-8 作为文件编码，请调整开发工具的相关设置以确保编码正确性，并保证编译时所使用的编码也是正常的。后一个目标通常可以通过设置  `JAVA_TOOL_OPTIONS` 以及 `GRADLE_OPTS` 两个环境变量的值为 `-Dfile.encoding=UTF-8` 来达成。

通常情况下，所有 Forge 模组都需要在源代码中添加一个类作为模组的「主类」——我们稍后会介绍模组的「主类」。

## 资源文件

Forge MDK 默认从 `src/main/resources` 和 `src/generated/resources` 检索资源文件，并几乎不加改动地复制到生成的模组文件中。

!> `src/generated/resources` 存放的是 Data Generator 自动生成的文件，因此开发者不应该对其中的资源进行手动调整，而仅应通过启动 Data Generator 更新资源，或将自己的资源放置于 `src/main/resources` 目录下。

除去 `pack.mcmeta` 和 `META-INF` 目录外，其他所有文件都应当位于 `assets` 和 `data` 目录下。

### `pack.mcmeta`

`pack.mcmeta` 是数据包和资源包共用的声明文件，为 [JSON 格式](https://www.json.org/json-zh.html)。以下为 `pack.mcmeta` 的一个示例：

```json
{
  "pack": {
    "pack_format": 8,
    "description": "Xiaozhong mod resources"
  }
}
```

?> 关于 `pack.mcmeta` 的具体格式，请参见 Minecraft Wiki 对[数据包](https://minecraft.fandom.com/zh/wiki/%E6%95%B0%E6%8D%AE%E5%8C%85)和[资源包](https://minecraft.fandom.com/zh/wiki/%E8%B5%84%E6%BA%90%E5%8C%85)的介绍。`pack_format` 指定了数据包和资源包的版本，对 Minecraft 1.18.x，其值为 `8`。

### `META-INF/mods.toml`

通常情况下，所有 Forge 模组都需要在 `META-INF` 目录下指定一个 [TOML 格式](https://toml.io/cn/)的 `mods.toml` 文件。`mods.toml` 文件指定了模组的相关信息。

Forge MDK 默认提供的 `mods.toml` 以注释的形式为其提供了详尽的解释。以下为 `mods.toml` 的一个示例：

```toml
modLoader="javafml" # 模组所使用的加载器，此处固定
loaderVersion="[39,)" # 加载器版本号，通常和 Forge 的大版本号有关
license="All rights reserved" # 模组所采用的授权协议

[[mods]] # 模组本体信息
modId="xiaozhong" # 模组 ID
version="${file.jarVersion}" # 模组的版本号，此处固定
authors="TeaConMC" # 模组的作者，可在此填写自己的常用名称
displayName="Xiaozhong" # 模组名称，通常和 build.gradle 所写相同
description="The example mod for xiaozhong" # 模组的相关介绍，可以多行

[[dependencies.cannon_fire]] # 模组的相关依赖，通常会写上对 Forge 版本的依赖
modId="forge" # 相关依赖的模组 ID
mandatory=true # 相关依赖是否为必须
versionRange="[39,)" # 相关依赖的版本号范围
ordering="NONE" # 相关依赖和模组本体的加载顺序，也可以是 BEFORE 或 AFTER
side="BOTH" # 相关依赖是否一定要在客户端或服务端出现，也可以是 CLIENT 或 SERVER
```

!> 模组开发者必须通过 `mods.toml` 指定一个协议，否则模组将无法启动。基于 TeaCon 的举办理念，我们鼓励模组开发者采用一个自由或开源的授权协议。

### `assets` 和 `data`

 `assets` 和 `data` 目录下的资源分别对应资源包和数据包的资源。二类资源均拥有对应的[资源路径](https://minecraft.fandom.com/zh/wiki/%E5%91%BD%E5%90%8D%E7%A9%BA%E9%97%B4ID)。

!> 为方便起见，本篇指南的后续内容均将使用资源路径代指资源，如使用「`minecraft:item/compass` 处的模型文件」代指「名为 `assets/minecraft/models/item/compass.json` 的模型文件」。部分情况下，开发者引用的资源路径需补齐前缀和后缀（使用  `minecraft:textures/block/stone.png` 而非 `minecraft:block/stone`），我们会在类似情况出现时特殊说明，请各位读者加以注意。

以下是 Minecraft 中的一些典型的资源类型：

| 资源性质 | 资源类型 | 资源前缀       | 资源后缀 | 示例                                                         |
| -------- | -------- | -------------- | -------- | ------------------------------------------------------------ |
| 资源包   | 模型     | `models/`      | `.json`  | 路径：`minecraft:item/compass`<br />文件：`assets/minecraft/models/item/compass.json` |
| 资源包   | 纹理     | `textures/`    | `.png`   | 路径：`minecraft:block/stone`<br />文件：`assets/minecraft/textures/block/stone.png` |
| 资源包   | 方块状态 | `blockstates/` | `.json`  | 路径：`minecraft:dirt`<br />文件：`assets/minecraft/blockstates/dirt.json` |
| 资源包   | 语言文件 | `lang/`        | `.json`  | 路径：`minecraft:zh_cn`<br />文件：`assets/minecraft/lang/zh_cn.json` |
| 数据包   | 配方     | `recipes/`     | `.json`  | 路径：`minecraft:bread`<br />文件：`data/minecraft/recipes/bread.json` |
| 数据包   | 战利品表 | `loot_tables/` | `.json`  | 路径：`minecraft:blocks/ice`<br />文件：`data/minecraft/loot_tables/blocks/ice.json` |

资源路径在源代码中为 `ResourceLocation`，如 `new ResourceLocation("foo", "bar")` 即代表 `foo:bar` 这一资源路径。

!> 模组 ID 是模组的唯一标识符，也应当是所有和模组相关的资源的[命名空间](https://minecraft.fandom.com/zh/wiki/%E5%91%BD%E5%90%8D%E7%A9%BA%E9%97%B4ID)：在管理资源时应当尽量使用模组 ID 作为命名空间。本篇指南所有新添加的资源均归属于 `xiaozhong` 命名空间。
