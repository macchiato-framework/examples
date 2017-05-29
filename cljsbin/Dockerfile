FROM java:8

LABEL name "cljsbin"

RUN apt-get update -y && \
apt-get install --no-install-recommends -y \
-q curl python build-essential git ca-certificates

# Install Node.js
ENV DEBIAN_FRONTEND noninteractive

RUN curl -sL https://deb.nodesource.com/setup_6.x | bash
RUN apt-get install -y nodejs
# TODO could uninstall some build dependencies

# fucking debian installs `node` as `nodejs`
RUN update-alternatives --install /usr/bin/node node /usr/bin/nodejs 10

# Install leiningen
RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > /bin/lein
RUN chmod a+x /bin/lein

ENV LEIN_ROOT=1
RUN lein

WORKDIR /
RUN git clone https://github.com/facundoolano/cljsbin.git

WORKDIR /cljsbin
RUN lein package

EXPOSE 3000
CMD npm start
