# Use java official image for building scala code
ARG BUILD_BASE=openjdk:11

# Create a user with restricted rights
FROM $BUILD_BASE AS password_donor
RUN useradd -o -u 1000 unpriv

# Create temporary image for building the project
FROM $BUILD_BASE AS build_base
COPY [the source code]
RUN [build the source code]

# Create the image that will host the services' executable
FROM scratch
COPY [the executable file]
WORKDIR [working directory]
USER unpriv
CMD ["/[the executable]"]

# TODO: Complete this docker file