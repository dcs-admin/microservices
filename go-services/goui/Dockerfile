# Stage 1: Build Angular app
FROM node:18 as builder

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod

# Stage 2: Serve with Nginx
FROM nginx:alpine

# Copy built app from builder
COPY --from=builder /app/dist/goui /usr/share/nginx/html

# Optionally add a custom nginx config
# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
