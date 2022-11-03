# Image names
IMG_DATABASE = image_database:0.0.1
IMG_ENCRYPTED_DATA_SENDER = image_encrypted_data_sender:0.0.1
IMG_USER_ID_MANAGER = image_user_id_manager:0.0.1
IMG_USER_REQUEST_FETCHER = image_user_request_fetcher:0.0.1
IMG_USER_REQUEST_MANAGER = image_user_request_manager:0.0.1
IMG_USER_RESPONSE = image_user_response:0.0.1
IMG_USER_UPDATE = image_user_update:0.0.1

# Fetch the Client database credentials
DB_USERNAME = dbadmin
DB_PASSWORD = null  	# This variable needs to be set when 'make' is called

all: run-unit-tests docker-build

run-unit-tests:
	echo "Running unit tests..."
	echo "Done running unit tests."

# Build the service images
docker-build:
	echo "Building the service images."
	docker build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_ENCRYPTED_DATA_SENDER --progress
	docker build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_ID_MANAGER --progress
	docker build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_REQUEST_FETCHER --progress
	docker build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_REQUEST_MANAGER --progress
	docker build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_RESPONSE --progress
	docker build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_UPDATE --progress
	echo "Done building the service images."
