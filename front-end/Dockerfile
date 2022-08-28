# syntax=docker/dockerfile:1

FROM node:18.7-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

RUN apk add --no-cache curl

COPY . ./

EXPOSE 4444

ENTRYPOINT ["npm", "start"]
