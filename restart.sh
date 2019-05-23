docker pull kolegran/spd-google

docker rm -f spd-google

docker run -d -p 80:8080 --name spd-google kolegran/spd-google:latest
