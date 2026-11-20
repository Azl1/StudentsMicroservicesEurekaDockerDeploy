icacls .\id_rsa /inheritance:r
icacls .\id_rsa /grant:r %USERNAME%:(R)

ssh -i .\id_rsa root2@IP_СЕРВЕРА
ssh -i .\id_rsa root2@194.113.34.190

df -h

curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

sudo fallocate -l 4G /swapfile && sudo chmod 600 /swapfile
sudo mkswap /swapfile && sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

mkdir -p /home/root2/app

scp -i .\id_rsa -r ./pom.xml ./Dockerfile ./docker-compose.yml ./src root2@IP_СЕРВЕРА:/home/root2/app/
scp -i .\id_rsa -r ./pom.xml ./Dockerfile ./docker-compose.yml ./src root2@194.113.34.190:/home/root2/app/
scp -i .\id_rsa -r .\AutosMicroservice .\config .\EurekaServer .\init .\StudentsMicroservice .\docker-compose.yml root2@194.113.34.190:/home/root2/app/ - если не получится, то тут более точный путь директории

cd /home/root2/app

sudo docker compose up --build -d

sudo docker compose logs -f spring-app

sudo docker compose ps

http://IP_СЕРВЕРА:8080/api/students
http://194.113.34.190:8080/api/students

sudo ufw allow 8080/tcp
