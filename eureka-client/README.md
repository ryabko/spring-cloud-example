# Eureka Client

### How to build

```bash
./mvnw package
```

```bash
docker build -t spring-cloud-example/eureka-client .
```

### How to run
Eureka Server should be started in advance.

```bash
docker run \
    --network spring-cloud-example \
    -e SERVER_PORT=8080 \
    -e EUREKA_URI=http://eureka-server:8761/eureka \
    -d --rm -p 8081:8080 \
    spring-cloud-example/eureka-client
 ```
`--network spring-cloud-example` means the container will be attached to the network "spring-cloud-example".
It's supposed that Eureka Server container was attached to the same network, 
so the Eureka Client will be able to find the Eureka Server by its hostname. 

`-e SERVER_PORT=8080` is an environment variable that says the web server should start on the port 8080.
It's required if you want to publish the port, because you need to know which port to publish.
If you don't set this variable, it will use a random port.
You don't have to set this variable if you don't publish the port.

`-e EUREKA_URI=http://eureka-server:8761/eureka` is an environment variable that specifies the URL of Eureka Server.
Default value uses `localhost` and it's not appropriate here since localhost refers to the current docker container
whereas Eureka Server is running in another docker container.
The hostname `eureka-server` is used, given that the docker container with Eureka Server
was started with the parameter `--hostname eureka-server`.

`-d` means run in background

`--rm` means remove the container when it's stopped

`-p` proxies container's port 8080 to the host port 8081, so Eureka Client can be accessed via `http://localhost:8081`.
Note, port publishing is not required, and it's used for debugging purpose only.  

If you want to run another instance of Eureka Client, 
use the same `docker run` command but use a different port instead of `8081`.
If you don't need to access Eureka Client directly, don't publish the port (don't specify `-p`),
and you can use exactly the same `docker run` command for all Eureka Client instances.

### Verification
If you publish the port (`-p` in `docker run` command), open `http://localhost:8081/hello`.
It might take time to register in Eureka Server and complete the Eureka Client initialization,
so it might return the error during the first minute.