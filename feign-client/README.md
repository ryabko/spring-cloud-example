# Feign Client

### How to build

```bash
./mvnw package
```

```bash
docker build -t spring-cloud-example/feign-client .
```

### How to run
Eureka Server and Eureka Clients (one or more instances) should be started in advance.

```bash
docker run \
    --network spring-cloud-example \
    -e EUREKA_URI=http://eureka-server:8761/eureka \
    -d --rm -p 8080:8080 \
    spring-cloud-example/feign-client
 ```
`--network spring-cloud-example` means the container will be attached to the network "spring-cloud-example".
It's supposed that Eureka Server and Eureka Client containers were attached to the same network,
so the Feign Client will be able to find the Eureka Server by its hostname and Eureka Clients by their hostnames or IPs.

`-e EUREKA_URI=http://eureka-server:8761/eureka` is an environment variable that specifies the URL of Eureka Server.
Default value uses `localhost` and it's not appropriate here since localhost refers to the current docker container
whereas Eureka Server is running in another docker container.
The hostname `eureka-server` is used, given that the docker container with Eureka Server
was started with the parameter `--hostname eureka-server`.

`-d` means run in background

`--rm` means remove the container when it's stopped

`-p` proxies container's port 8080 to the host port 8080, so Feign Client can be accessed via `http://localhost:8080`.

### Verification
Open `http://localhost:8080/hello`.
It should return the hello message from one of the Eureka Clients.
If you have multiple running instances of Eureka Client, refresh the page,
and it will return a message from another instance (you'll see another instanceId).

Open `http://localhost:8080/info`.
It will show the host and port of the first Eureka Client instance. 
Also, it will show how many instances of Eureka Client are running.  

Feign Client talks to the Eureka Server and asks which Eureka Clients are registered for the application.
Eureka Server returns a list of registered instances, each instance has a host and port.
Feign Client chooses one of the instances and uses its host and port to execute a request.