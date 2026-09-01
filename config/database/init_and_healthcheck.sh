#!/bin/bash

create_postgres_databases() {
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_auth_server_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_cart_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_customer_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_discount_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_inventory_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_mail_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_order_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_payment_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_shipping_service_db;"
  psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE ecommerce_upload_service_db;"
}

echo "Creating databases..."

# Calling the above function
create_postgres_databases

# Function to check postgres health
check_postgres_health() {
  pg_isready -U $POSTGRES_USER
  return $?
}

# Wait for postgres to be ready
until check_postgres_health; do
  echo "Waiting for postgres to be ready.."
  sleep 2
done

echo "Postgres is ready. Running health check..."

#Perform health check by verifying the databases exist
DATABASES=(
  "ecommerce_auth_server_db"
  "ecommerce_cart_service_db"
  "ecommerce_customer_service_db"
  "ecommerce_discount_service_db"
  "ecommerce_inventory_service_db"
  "ecommerce_mail_service_db"
  "ecommerce_order_service_db"
  "ecommerce_payment_service_db"
  "ecommerce_shipping_service_db"
  "ecommerce_upload_service_db"
)

for db in "${DATABASES[@]}"; do
  if psql -U $POSTGRES_USER -d $POSTGRES_DB -c "SELECT 1 FROM pg_database WHERE datname='$db';" | grep -q 1; then
    echo "Database $db exists."
  else
    echo "Database $db does not exist."
    exit 1
  fi
done

echo "All databases exist. Health check passed."
exit 0