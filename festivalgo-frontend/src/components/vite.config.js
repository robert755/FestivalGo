import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,         // obligă vite să pornească doar pe acest port
    strictPort: true    // dacă portul e ocupat, aruncă eroare (nu schimbă la 5174)
  }
})
