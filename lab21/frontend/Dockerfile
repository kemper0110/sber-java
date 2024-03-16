FROM node:21-alpine as frontend-build
RUN npm i -g npm
WORKDIR /usr/app
COPY . .
RUN npm i
RUN npm run build

FROM nginx:stable-alpine3.17-slim
RUN apk add curl
COPY --from=frontend-build /usr/app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf