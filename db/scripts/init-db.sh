#!/bin/bash

psql -v ON_ERROR_STOP=1 --username postgres --dbname postgres <<-EOSQL
  CREATE DATABASE exchange_service WITH OWNER postgres;
EOSQL

export PGPASSWORD=postgres
