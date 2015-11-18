FROM degree9/heroku-boot-clj

ENV BOOT_VERSION=2.4.2

# install git
RUN apt-get install -y git

# install node
RUN curl --silent --location https://deb.nodesource.com/setup_0.12 | bash -
RUN apt-get install -y nodejs

# install bower
RUN npm install --global bower

RUN mkdir -p /app/user/d9lounge

COPY build.boot /app/user/d9lounge

WORKDIR /app/user/d9lounge

COPY /resources /app/user/d9lounge

RUN boot build-bower

COPY /src /app/user/d9lounge

RUN boot build

VOLUME ["/app/user/d9lounge"]

EXPOSE 8080
