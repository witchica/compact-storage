import { defineVersionedConfig } from '@viteplus/versions';

// https://vitepress.dev/reference/site-config
export default defineVersionedConfig({
  title: "CompactStorage",
  description: "Expandable storage for Minecraft 26.1",
  versionsConfig: {
    current: "26.1.x",
    versionSwitcher: {
      text: "Version",
      includeCurrentVersion: true
    }
  },
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Examples', link: '/markdown-examples' }
    ],

    sidebar: [
      {
        text: 'Examples',
        items: [
          { text: 'Markdown Examples', link: '/markdown-examples' },
          { text: 'Runtime API Examples', link: '/api-examples' }
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/witchica/compact-storage' }
    ]
  }
})
