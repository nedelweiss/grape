docker pull kolegran/spd-google

docker rm -f spd-google

docker run -d -p 8082:8080 --name spd-google kolegran/spd-google:latest
