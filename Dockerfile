FROM adzerk/boot-clj:latest

ENV BOOT_VERSION=2.4.2

RUN apt-get update \
    && apt-get install -y git curl

# install npm
RUN apt-get install -y npm
RUN ln -s /usr/bin/nodejs /usr/bin/node

# install bower
RUN npm install --global bower

RUN mkdir -p /usr/src/d9lounge

COPY build.boot /usr/src/d9lounge/

WORKDIR /usr/src/d9lounge

RUN boot

COPY . /usr/src/d9lounge

VOLUME ["/usr/src/d9lounge"]

EXPOSE 80

CMD ["boot" "prod"]


