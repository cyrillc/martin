#!/usr/bin/env bash

# Copy settings for maven-build and depoly
sudo cp ${BASH_SOURCE%/*}/settings.xml /var/lib/jenkins/.m2/settings.xml
sudo cp ${BASH_SOURCE%/*}/pwdfile /var/lib/jenkins/.m2/pwdfile


# Start Glassfish
/home/vagrant/glassfish4/bin/asadmin --passwordfile /home/vagrant/pwdfile start-domain
