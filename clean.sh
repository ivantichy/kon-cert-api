docker ps -aq | xargs docker rm 
docker images -q | xargs docker rmi


