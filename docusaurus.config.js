// @ts-check

const lightCodeTheme = require('prism-react-renderer/themes/github')
const darkCodeTheme = require('prism-react-renderer/themes/dracula')

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: '正山小种 - Forge 模组开发指南',
  tagline: '一篇以尽可能简洁的篇幅带领 Minecraft 玩家入门的 Forge 模组开发指南',
  url: 'https://www.teacon.cn/',

  trailingSlash: false,
  baseUrl: '/xiaozhong/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'zh',
    locales: ['zh'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        blog: false,
        docs: {
          routeBasePath: '/',
          sidebarPath: require.resolve('./src/sidebars.js'),
          editUrl({ docPath }) {
            return 'https://github.com/teaconmc/Xiaozhong/blob/1.19-forge/docs/' + docPath
          }
        },
        theme: {
          customCss: [require.resolve('./src/custom.css')],
        },
      }),
    ],
  ],

  themes: [
    [
      "@easyops-cn/docusaurus-search-local",
      {
        hashed: true,
        language: ['en', 'zh'],
        docsRouteBasePath: '/',
        explicitSearchResultPath: true,
        highlightSearchTermsOnTargetPage: true,
      }
    ]
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      navbar: {
        title: '正山小种 - Forge 模组开发指南',
        items: [
          {
            type: 'doc',
            position: 'right',
            docId: '1.19.x/README',
            label: '1.19.x',
          },
          {
            type: 'doc',
            position: 'right',
            docId: '1.18.x/README',
            label: '1.18.x',
          },
          {
            position: 'right',
            href: 'https://github.com/teaconmc/Xiaozhong',
            label: 'GitHub',
          },
        ],
      },
      footer: {
        style: 'dark',
        copyright: ''.concat(
          `Copyright ©2022-${Math.min(2023, new Date().getFullYear())} TeaCon 执行委员会`, ` | `,
          `<a href="https://beian.miit.gov.cn/" style="color:var(--ifm-footer-color)">闽ICP备20015816号</a>`,
        ),
      },
      prism: {
        theme: lightCodeTheme,
        darkTheme: darkCodeTheme,
        additionalLanguages: ['bash', 'groovy', 'java', 'json', 'toml', 'kotlin'],
      },
    }),
}

module.exports = config
