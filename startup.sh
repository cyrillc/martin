#!/usr/bin/env bash

/home/vagrant/glassfish4/bin/asadmin --passwordfile /home/vagrant/pwdfile start-domain


# Start and Configure Jenkins service
/vagrant/config/vagrant_config/jenkins/init_jenkins.sh

echo "Visit http://localhost:8080 for Jenkins"
echo "Visit http://localhost:4848 for GlassFish"
echo "Visit http://localhost:4040 for Project-Site"
