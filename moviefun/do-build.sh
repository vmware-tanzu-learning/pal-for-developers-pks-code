mvn clean package -DskipTests -Dmaven.test.skip=true
docker build --build-arg MOVIEFUN_VERSION=2.0 -t pivotaleducation/moviefun-pks:2.0 .
docker build --build-arg MOVIEFUN_VERSION=3.0 -t pivotaleducation/moviefun-pks:3.0 .
docker push pivotaleducation/moviefun-pks:2.0
docker push pivotaleducation/moviefun-pks:3.0
docker image rm pivotaleducation/moviefun-pks:2.0
docker image rm pivotaleducation/moviefun-pks:3.0