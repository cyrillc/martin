#!/usr/bin/env bash

# Install nodejs (might be changed to pre-installed in new box)
# /vagrant/config/vagrant_config/nodejs/init_nodejs.sh

# Start and Configure Jenkins service
/vagrant/config/vagrant_config/jenkins/init_jenkins.sh

# Start MySQL Service
# /vagrant/config/vagrant_config/mysql/init_mysql.sh


echo "Visit http://localhost:8080 for Jenkins"
echo "Visit http://localhost:4040 for Project-Site"
echo "MySQL is Running on Port 3306 - To connect you can use something like MySQL Workbench"
