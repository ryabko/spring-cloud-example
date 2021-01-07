# Zuul Load Balancer

### How to build

```bash
./mvnw package
```

```bash
docker build -t spring-cloud-example/zuul .
```

### How to run
Eureka Server and Eureka Clients (one or more instances) should be started in advance.

```bash
docker run \
    --network spring-cloud-example \
    -e EUREKA_URI=http://eureka-server:8761/eureka \
    -d --rm -p 8762:8762 \
    spring-cloud-example/zuul
 ```
`--network spring-cloud-example` means the container will be attached to the network "spring-cloud-example".
It's supposed that Eureka Server and Eureka Client containers were attached to the same network,
so Zuul will be able to find the Eureka Server by its hostname and Eureka Clients by their hostnames or IPs.

`-e EUREKA_URI=http://eureka-server:8761/eureka` is an environment variable that specifies the URL of Eureka Server.
Default value uses `localhost` and it's not appropriate here since localhost refers to the current docker container
whereas Eureka Server is running in another docker container.
The hostname `eureka-server` is used, given that the docker container with Eureka Server
was started with the parameter `--hostname eureka-server`.

`-d` means run in background

`--rm` means remove the container when it's stopped

`-p` proxies container's port 8762 to the host port 8762, so Zuul can be accessed via `http://localhost:8762`.

### Verification
Open `http://localhost:8762/actuator/routes`.
It should return a list of routes that Zuul automatically configured based on the data from Eureka Server.
It should have the route `/spring-cloud-eureka-client/**` 

Open `http://localhost:8762/spring-cloud-eureka-client/hello`.
It should return the hello message from one of the Eureka Clients.
If you have multiple running instances of Eureka Client, refresh the page,
and it will return a message from another instance (you'll see another instanceId).

Zuul talks to the Eureka Server and asks which Eureka Clients are registered for the application.
Eureka Server returns a list of registered instances, each instance has a host and port.
Zuul chooses one of the instances and uses its host and port to execute a request.