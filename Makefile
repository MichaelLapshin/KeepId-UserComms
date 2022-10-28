# Image names
IMG_DATABASE = "image_database"
IMG_ENCRYPTED_DATA_SENDER = "image_encrypted_data_sender"
IMG_USER_ID_MANAGER = "image_user_id_manager"
IMG_USER_REQUEST_FETCHER = "image_user_request_fetcher"
IMG_USER_REQUEST_MANAGER = "image_user_request_manager"
IMG_USER_RESPONSE = "image_user_response"
IMG_USER_UPDATE = "image_user_update"

TAG = "0.0.1"

# Build the service images
docker-build:
	docker image build --target /src/common/constants/database_instance/Dockerfile --file $IMG_DATABASE --tag $TAG --progress
	docker image build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_ENCRYPTED_DATA_SENDER --tag $TAG --progress
	docker image build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_ID_MANAGER --tag $TAG --progress
	docker image build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_REQUEST_FETCHER --tag $TAG --progress
	docker image build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_REQUEST_MANAGER --tag $TAG --progress
	docker image build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_RESPONSE --tag $TAG --progress
	docker image build --target /src/services/encrypted_data_sender/Dockerfile --file $IMG_USER_UPDATE --tag $TAG --progress
