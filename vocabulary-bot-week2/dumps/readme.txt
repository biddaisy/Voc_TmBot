docker run -d -p 27017:27017 --name mongodb mongo:focal
docker run -d -p 27017:27017 --name mongodb -v mongodb_data:/data/db mongo:focal
docker-compose --project-name mongodb up -d