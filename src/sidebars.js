// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    '1.18.x': [{
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
    '1.19.4': [{
        type: 'category',
        label: '正山小种 - 1.19.4',
        collapsed: false,
        collapsible: false,
        link: {
            type: 'doc',
            id: '1.19.4/README'
        },
        items: [
            '1.19.4/preparations/README',
            '1.19.4/concepts/README',
            '1.19.4/block-item-objects/README',
            '1.19.4/creative_mode_tab/README',
            '1.19.4/localization-with-json/README',
            '1.19.4/block-entity/README',
            '1.19.4/world-gen/README',
        ],
    }],
};

module.exports = sidebars;
