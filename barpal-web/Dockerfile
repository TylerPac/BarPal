# Use official Node.js image as the build environment
FROM node:20-alpine as build
WORKDIR /app
# Copy only package manifests first for better layer caching
COPY package*.json ./
RUN npm install --production=false

# Copy application source (exclude via .dockerignore what we don't need)
COPY . .

# Ensure react-scripts is executable (handle rare permission issues)
RUN chmod +x node_modules/.bin/* || true

RUN npm run build

# Use Nginx to serve the build
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
# Healthcheck (basic) - adjust to real endpoint if needed
HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD wget -q -O /dev/null http://localhost/ || exit 1
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
