#!/bin/bash

# Stop jenkins if running
sudo /etc/init.d/jenkins stop

# Clean directories
sudo rm -rf /var/lib/jenkins/jobs/
sudo rm -rf /var/lib/jenkins/workspace/

# Start Jenkins service
sudo /etc/init.d/jenkins start

# Import all config-files in /jobs directory
echo -n 'Waiting for Jenkins to start...'
until $(curl --output /dev/null --silent --head --fail http://localhost:8080); do
    sleep 1
done
echo "\n"

echo "Importing jobs."
for filename in ${BASH_SOURCE%/*}/jobs/*.xml; do
	echo "    Importing $(basename "$filename" .xml)"
    java -jar ${BASH_SOURCE%/*}/jenkins-cli.jar -s http://localhost:8080/ create-job $(basename "$filename" .xml) < $filename
done

# Start initial build
echo "Starting initial build & deployment"
while read p; do
	echo "    Kick off build for $p"
  	java -jar ${BASH_SOURCE%/*}/jenkins-cli.jar -s http://localhost:8080/ build "$p"
done < ${BASH_SOURCE%/*}/jobs/startup.conf 

java -jar ${BASH_SOURCE%/*}/jenkins-cli.jar -s http://localhost:8080/ build $(basename "$filename" .xml)