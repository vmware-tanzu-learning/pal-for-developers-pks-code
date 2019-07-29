# Movie Fun!

# step by step build and publish

Smoke Tests require server running on port 8080 by default.

## Build WAR ignoring Smoke Tests

```bash
mvn clean package -DskipTests -Dmaven.test.skip=true
```

## Run it
```bash
java -jar target/moviefun.war
```

## Verify it
```bash
open http://localhost:8080
```

## Run Smoke Tests against specific URL
```bash
MOVIE_FUN_URL=http://localhost:8080 mvn test
```
