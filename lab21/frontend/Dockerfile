ARG API_URL

FROM node:21-alpine as frontend-build
RUN npm i -g npm
WORKDIR /usr/app
COPY package.json package-lock.json ./
RUN npm i
COPY . .
RUN npm run build

FROM nginx:stable-alpine3.17-slim
RUN apk add curl
COPY --from=frontend-build /usr/app/dist /usr/share/nginx/html
ENV API_URL ${API_URL}
COPY nginx.template.conf /nginx.conf.template

CMD /bin/sh -c "envsubst '\$API_URL' < /nginx.conf.template > /etc/nginx/nginx.conf && exec nginx -g 'daemon off;'"