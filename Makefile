# Creates local docker images for all services
VERSION = local

all: build-service-jars build-docker-images

build-service-jars:
	echo " ### KeepId ### Building the service jars..."
	sbt compile assembly

build-docker-images:
	echo " ### KeepId ### Building the docker image for each service..."
	docker build company_request_manager/Dockerfile -t company_request_manager:${VERSION}
	docker build encrypted_data_sender/Dockerfile -t encrypted_data_sender:${VERSION}
	docker build user_id_manager/Dockerfile -t user_id_manager:${VERSION}
	docker build user_request_fetcher/Dockerfile -t user_request_fetcher:${VERSION}
	docker build user_request_manager/Dockerfile -t user_request_manager:${VERSION}
	docker build user_response/Dockerfile -t user_response:${VERSION}
	docker build user_update/Dockerfile -t user_update:${VERSION}

.PHONY: clean
clean:
	echo " ### KeepId ### Cleaning the project..."
	sbt clean
