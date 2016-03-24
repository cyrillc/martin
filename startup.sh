#!/usr/bin/env bash

# Start and configure Glassfish
/vagrant/config/vagrant_config/glassfish/init_glassfish.sh


# Start and Configure Jenkins service
/vagrant/config/vagrant_config/jenkins/init_jenkins.sh

echo "Visit http://localhost:8080 for Jenkins"
echo "Visit http://localhost:4848 for GlassFish"
echo "Visit http://localhost:4040 for Project-Site"
echo "MySQL is Running on Port 3306 - To connect you can use something like MySQL Workbench"
