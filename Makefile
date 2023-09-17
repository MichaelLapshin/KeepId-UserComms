# Creates local docker images for all services
VERSION = local

all: build-service-jars build-docker-images

build-service-jars:
	echo "	### KeepId ### Building the service jars..."
	sbt compile assembly

build-docker-images:
	echo "	### KeepId ### Building the docker image for each service..."
#	docker build -t company_request_manager:${VERSION} company_request_manager/
#	docker build -t encrypted_data_sender:${VERSION} encrypted_data_sender/
	docker build -t user_id_manager:${VERSION} user_id_manager/
#	docker build -t user_request_fetcher:${VERSION} user_request_fetcher/
#	docker build -t user_request_manager:${VERSION} user_request_manager/
#	docker build -t user_response:${VERSION} user_response/
#	docker build -t user_update:${VERSION} user_update/

.PHONY: clean
clean:
	echo "	### KeepId ### Cleaning the project..."
	sbt clean
