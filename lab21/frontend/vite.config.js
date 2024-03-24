import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

const app_version = process.env.npm_package_version

// https://vitejs.dev/config/
export default defineConfig({
  define: {
    '__APP_VERSION__': JSON.stringify(app_version)
  },
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080/",
        changeOrigin: true,
        secure: false,
        configure: (proxy, _options) => {
          proxy.on('error', (err, _req, _res) => {
            console.log('proxy error', err);
          });
          proxy.on('proxyReq', (_proxyReq, req, _res) => {
            console.log('Sending Request to the Target:', req.method, req.url);
          });
          proxy.on('proxyRes', (proxyRes, req, _res) => {
            console.log('Received Response from the Target:', proxyRes.statusCode, req.url);
          });
        },
      }
    }
  },
  build: {
    emptyOutDir: true,
    rollupOptions: {
      output: {
        entryFileNames: () => `${app_version}/[name]-[hash].js`,
        chunkFileNames: () => `${app_version}/[name]-[hash].js`,
        assetFileNames: () => `${app_version}/[name]-[hash][extname]`,
      },
    },
  }
})
