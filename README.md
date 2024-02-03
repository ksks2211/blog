## BLOG
> spring boot markdown blog

### env
- env.list
- application.yml
  - cors.allowed-origins

### create docker image
> docker build -t spring_blog ./

### docker run
> docker run -d --network mynetwork --name  spring_blog --env-file ./env.list -p 8080:8080 spring_blog