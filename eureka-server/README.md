# Eureka Server

### How to build

```bash
./mvnw package
```

```bash
docker build -t spring-cloud-example/eureka-server .
```

### How to run
```bash
docker network create spring-cloud-example
```
This command creates a custom docker network that will be used for each container.
Default Docker bridge network doesn't have internal dns and doesn't allow one container 
to find another container by its hostname.  

```bash
docker run \
    --network spring-cloud-example \
    --hostname eureka-server \
    -d --rm -p 9761:8761 \
    spring-cloud-example/eureka-server
 ```
`--network spring-cloud-example` means the container will be attached to the network "spring-cloud-example".
All other containers should be started with the same argument.

`--hostname eureka-server` means this container will have the hostname `eureka-server` in internal docker network.
Other docker containers can use this name to access eureka-server. 

`-d` means run in background

`--rm` means remove the container when it's stopped

`-p` proxies container's port 8761 to the host port 9761, so Eureka UI can be open via `http://localhost:9761`.
Note, port publishing is not required, and it's used for debugging purpose only.

### Verification
If you publish the port (`-p` in `docker run` command), open `http://localhost:9761`.