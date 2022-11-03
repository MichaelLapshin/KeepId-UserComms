# KeepId-Comms repository:
This project contains the source code for creating the services and storage volumes surrounding the keep.
The services are developed in Scala.

# Setup
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