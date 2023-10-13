# Define your targets and their commands

# Build the Spring Boot application
build:
	./gradlew build

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