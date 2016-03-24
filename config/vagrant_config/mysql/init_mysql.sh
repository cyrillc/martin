#!/usr/bin/env bash

# Make shure the MySQL Server is not running
sudo /etc/init.d/mysql stop

# Backup my.cnf
sudo mv /etc/mysql/my.cnf /etc/mysql/my.cnf.backup

# Load our config
sudo cp /vagrant/config/vagrant_config/mysql/my.cnf /etc/mysql/my.cnf

# Reboot Network Service
sudo /etc/init.d/networking stop
sudo /etc/init.d/networking start

# Start MySQL Service on Server
sudo /etc/init.d/mysql start

# Anders funktioniert es nicht - sorry
sudo /etc/init.d/mysql stop
sudo initctl --system stop mysql
sudo /etc/init.d/mysql start

