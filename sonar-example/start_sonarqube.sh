#!/bin/bash

sudo sysctl -w vm.max_map_count=262144
docker-compose --project-name archunit-sonarqube -f ./docker-compose.yml up
