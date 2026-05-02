// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    'xiaozhong': [{
        type: 'category',
        label: '正山小种 - 26.1 + NeoForge',
        collapsed: false,
        collapsible: false,
        link: {
            type: 'doc',
            id: '26.1-neoforge/README'
        },
        items: [
            '26.1-neoforge/preparations/README',
            '26.1-neoforge/concepts/README',
            '26.1-neoforge/block-item-objects/README',
            '26.1-neoforge/localization-with-json/README',
            '26.1-neoforge/block-entity/README',
            '26.1-neoforge/world-gen/README',
        ],
    }, {
        type: 'category',
        label: '正山小种 - 1.21 + NeoForge',
        collapsed: false,
        collapsible: false,
        link: {
            type: 'doc',
            id: '1.21-neoforge/README'
        },
        items: [
            '1.21-neoforge/preparations/README',
            '1.21-neoforge/concepts/README',
            '1.21-neoforge/block-item-objects/README',
            '1.21-neoforge/localization-with-json/README',
            '1.21-neoforge/block-entity/README',
            '1.21-neoforge/world-gen/README',
        ],
    }, {
        type: 'category',
        label: '正山小种 - 1.19.x',
        collapsed: false,
        collapsible: false,
        link: {
            type: 'doc',
            id: '1.19.x/README'
        },
        items: [
            '1.19.x/preparations/README',
            '1.19.x/concepts/README',
            '1.19.x/block-item-objects/README',
            '1.19.x/localization-with-json/README',
            '1.19.x/block-entity/README',
            '1.19.x/world-gen/README',
        ],
    }, {
        type: 'category',
        label: '正山小种 - 1.18.x',
        collapsed: false,
        collapsible: false,
        link: {
            type: 'doc',
            id: '1.18.x/README'
        },
        items: [
            '1.18.x/preparations/README',
            '1.18.x/concepts/README',
            '1.18.x/block-item-objects/README',
            '1.18.x/localization-with-json/README',
            '1.18.x/block-entity/README',
            '1.18.x/world-gen/README',
        ],
    }],
};

module.exports = sidebars;
