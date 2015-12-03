FROM degree9/heroku-boot-clj:latest

ENV BOOT_VERSION=2.4.2

# install node
RUN curl --silent --location https://deb.nodesource.com/setup_0.12 | bash - \
 && apt-get install -y nodejs

# install bower
RUN npm install --global bower

# move boot files
COPY build.boot /app/user

COPY boot.properties /app/user

RUN boot repl -e '(System/exit 0)'

# move project files
COPY . /app/user

RUN boot build
