docker pull kolegran/spd-google

docker rm -f spd-google

docker run -d -p 8080:8080 --name spd-google kolegran/spd-google:latest
