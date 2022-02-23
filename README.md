# demo-movie-service

Services deployment
```
cd [the_directory_of_movie-services]
docker compose up -d
```

Android setup
```
open com.example.movieapp.service HttpService.java 
update the ip of movie-service
```

## Introduction
This is a small practice about spring microservices and distributed systems by implementing the search movie function. 


### Overall design
- Eureka - Service Registry and Discovery
- Zipkin - Distributed Tracing
- Spring - The framework then support the microservices
- Docker - Deployment
- Postgres - Database
- pgAdmin - Database UI management tools
- Android - The frontend Application

### Microservices
- movie-service - Search, Remove, Update, Rate Movie
- omdb-service - FEtch data from OMDB Api

### Flow
![backend-diagram](https://user-images.githubusercontent.com/17758393/155372217-f7405e8e-085a-4466-9b6b-9628f1b52743.jpg)



