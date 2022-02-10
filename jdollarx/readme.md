## simple run in Docker

```bash
docker build . -t test
docker run  -it test

```

## run in Docker and save reference images
In Java :
```java 
  SingltonBrowserImage img = new SingltonBrowserImage(el);
  img.captureToFile(new File("/images/docker-capture.png"));
```


```bash
docker run -v /directory/to/put/images/in:/images  -it test
```


## To run from within the container:

```bash
docker run -v /directory/to/put/images/in:/images  -it test /bin/bash

# inside the container :
mvn verify -Pfailsafe
```
