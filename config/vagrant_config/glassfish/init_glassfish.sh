#!/usr/bin/env bash

# Copy settings for maven-build and depoly
sudo cp ${BASH_SOURCE%/*}/settings.xml /var/lib/jenkins/.m2/settings.xml


# Start Glassfish
/home/vagrant/glassfish4/bin/asadmin --passwordfile /home/vagrant/pwdfile start-domain
