import { defineConfig, splitVendorChunkPlugin } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react(), splitVendorChunkPlugin()],
  server: {
    host: "0.0.0.0",
    port: 4444,
  },
});
