FROM openjdk:15-buster

#install chrome
RUN apt update -qq -y \
  && apt install -qq -y gnupg \
  && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list \
  && curl -O https://dl.google.com/linux/linux_signing_key.pub \
  && apt-key add linux_signing_key.pub \
  && apt update -qq -y && apt install -qq -y google-chrome-stable unzip \
  && mkdir -p /var/run/dbus \
  && apt install -qq -y maven

RUN CHROME_VERSION=`google-chrome --version | sed "s/[^0-9]*//" | sed "s/\..*$//"`; \
    CHROMEDRIVER_LATEST=$(curl -s https://chromedriver.storage.googleapis.com/LATEST_RELEASE_${CHROME_VERSION}); \
    curl -s -O https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_LATEST/chromedriver_linux64.zip \
    && unzip -o chromedriver_linux64.zip \
    && rm chromedriver_linux64.zip

COPY . /

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

ENV CHROMEDRIVERPATH /chromedriver
ENV HEADLESS_TESTING true

WORKDIR jdollarx

CMD mvn verify -Pfailsafe

