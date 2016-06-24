#!/usr/bin/env bash
rm -rf "plugins/zhaw.picturePlugin"
mvn liquibase:dropAll liquibase:update