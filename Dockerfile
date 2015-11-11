FROM adzerk/boot-clj:latest

ENV BOOT_VERSION=2.4.2

# install git
RUN apt-get install -y git

# install node
RUN curl --silent --location https://deb.nodesource.com/setup_0.12 | bash -
RUN apt-get install -y nodejs

# install bower
RUN npm install --global bower

RUN mkdir -p /usr/src/d9lounge

COPY build.boot /usr/src/d9lounge/

WORKDIR /usr/src/d9lounge

RUN boot

COPY . /usr/src/d9lounge

VOLUME ["/usr/src/d9lounge"]

EXPOSE 80

CMD ["prod"]
