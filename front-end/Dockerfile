# syntax=docker/dockerfile:1

FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
COPY . ./
RUN npm install
RUN npm run build

FROM joseluisq/static-web-server:2-alpine
RUN adduser -S app
USER app
COPY --from=builder /app/dist/ /public/
