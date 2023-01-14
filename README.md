# KeepId-UserComms repository:
This project contains the source code for creating the services and storage volumes surrounding the keep.
The services are developed in Scala.

# Service Deployment
* Compile all the services with the linux command ```sbt clean compile assembly```.
* Create an image of a particular service by running ```docker build [path_to_service_dockerfile] -t [image_tag]```.
* Deploy an image with ```docker run [image_name:tag]```.
* Deploy all the services with Kubernetes with command ```kubectl apply -f [k8s_resources_file]```.

# Setup (deprecated, not applicable)
* Clone all KeepId repositories on the same level of a directory.
* Create a directory called "secrets" and create/import the following files with content:
  * 'credentials.yaml'
  ```
  credentials:
    comms-database:
      username: dbadmin
      password: [db_admin_password]
    the-keep-database:
      username: dbadmin
      password: [db_admin_password]
  ```
  * '.netrc'
    * Create a GitHub token for all KeepId repositories with only ```read-only``` action permissions.
    * Enter the command ```chmod 400 .netrc``` to set it to read-only.
  ```
  machine github.com
  login [your_username]
  password [the_github_token]
  ```
* Set the following environment variable:
  * ```export KEEPID_SECRETS_DIR=/[path_to_secrets]/secrets```

# Deployment
* Run ```kubectl apply -f /[...]/KeepId-UserComms/k8s-resources-[prod|dev].yaml``` to deploy kubernetes with the KeepId communication services.