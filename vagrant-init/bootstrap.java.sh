sudo apt-get -y install openjdk-7-jdk
sudo apt-get -y install maven2

cd /vagrant/formatters/java-marshaller && mvn package && cd target && rm java-marshaller.jar && mv java-marshaller-jar-with-dependencies.jar java-marshaller.jar
