FROM openjdk:8-jre
ADD target/moviefun.war moviefun.war
EXPOSE 8080
ARG MOVIEFUN_VERSION
ENV MOVIEFUN_VERSION=$MOVIEFUN_VERSION
ENTRYPOINT ["java","-jar","/moviefun.war"]
