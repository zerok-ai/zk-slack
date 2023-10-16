# Define your targets and their commands
NAME = zk-slack
IMAGE_NAME = zk-slack
IMAGE_VERSION = 1.0
IMAGE_VERSION_MULTI_ARCH = multiarch

LOCATION ?= us-west1
PROJECT_ID ?= zerok-dev
REPOSITORY ?= zk-slack

BUILDER_NAME = multi-platform-builder
IMAGE_PREFIX := $(LOCATION)-docker.pkg.dev/$(PROJECT_ID)/$(REPOSITORY)/


# Build the Spring Boot application
build:
	./gradlew build

docker-build:
	./gradlew clean build --exclude-task test
	docker build --no-cache -t $(IMAGE_PREFIX)$(IMAGE_NAME):$(IMAGE_VERSION) .

# Run the Spring Boot application
run:
	java -jar build/libs/zk-slack-0.0.1-SNAPSHOT.jar

# Clean the build
clean:
	./gradlew clean

# Define additional targets for testing, etc.

# ------- CI-CD ------------
ci-cd-build:
	./gradlew clean build --exclude-task test