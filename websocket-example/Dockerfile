FROM mhart/alpine-node:latest

MAINTAINER Your Name <you@example.com>

# Create app directory
RUN mkdir -p /websocket-example
WORKDIR /websocket-example

# Install app dependencies
COPY package.json /websocket-example
RUN npm install pm2 -g
RUN npm install

# Bundle app source
COPY target/release/websocket-example.js /websocket-example/websocket-example.js
COPY public /websocket-example/public

ENV HOST 0.0.0.0

EXPOSE 3000
CMD [ "pm2-docker", "/websocket-example/websocket-example.js" ]
