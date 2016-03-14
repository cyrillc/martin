#!/usr/bin/env bash

/home/vagrant/glassfish4/bin/asadmin --passwordfile /home/vagrant/pwdfile start-domain
sudo /etc/init.d/jenkins start

