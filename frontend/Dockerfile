# ===============================
# Step 1 : Build Angular
# ===============================
FROM node:24-alpine AS build

WORKDIR /app

# Copy dependency files
COPY package*.json ./

# Installer les dépendances
RUN npm ci

# Copier le code source
COPY . .

# Build Angular (production)
RUN npm run build

# ===============================
# Step 2 : Nginx server
# ===============================
FROM nginxinc/nginx-unprivileged:alpine

# Remove default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Ajouter la configuration Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copier le build Angular
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Exposer le port
EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
