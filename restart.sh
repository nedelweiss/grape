docker pull kolegran/grape
docker rm -f grape

docker run -d -p 8082:8080 --name grape-search kolegran/grape:latest
