// @ts-check

const lightCodeTheme = require('prism-react-renderer/themes/github')
const darkCodeTheme = require('prism-react-renderer/themes/dracula')

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: '正山小种',
  tagline: 'Dinosaurs are cool',
  url: 'https://www.teacon.cn/',

  baseUrl: '/xiaozhong/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'zh-Hans',
    locales: ['zh-Hans'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          routeBasePath: '/',
          sidebarPath: require.resolve('./sidebars.js'),
          editUrl: 'https://github.com/teaconmc/Xiaozhong',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      navbar: {
        title: '正山小种',
      },
      footer: {
        style: 'dark',
        copyright: ''.concat(
          `Copyright ©${new Date().getFullYear()} TeaCon 执行委员会`, ` | `,
          `<a href="https://beian.miit.gov.cn/" style="color:var(--ifm-footer-color)">闽ICP备20015816号</a>`,
        ),
      },
      prism: {
        theme: lightCodeTheme,
        darkTheme: darkCodeTheme,
        additionalLanguages: ['bash', 'groovy', 'java', 'json', 'toml'],
      },
    }),
}

module.exports = config
