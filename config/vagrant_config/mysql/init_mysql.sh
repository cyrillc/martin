#!/usr/bin/env bash

# Make shure the MySQL Server is not running
sudo /etc/init.d/mysql stop

# Backup my.cnf
sudo mv /etc/mysql/my.cnf /etc/mysql/my.cnf.backup

# Load our config
sudo cp /vagrant/config/vagrant_config/mysql/my.cnf /etc/mysql/my.cnf


# Start MySQL Service on Server
sudo /etc/init.d/mysql start

# The Hack just works with a Restart at the end

sudo reboot
