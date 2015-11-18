FROM degree9/heroku-boot-clj

ENV BOOT_VERSION=2.4.2

# install node
RUN curl --silent --location https://deb.nodesource.com/setup_0.12 | bash - \
 && apt-get install -y nodejs

# install bower
RUN npm install --global bower

COPY build.boot /app/user

COPY . /app/user

RUN boot build
