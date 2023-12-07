# Use an official image of OpenJDK
FROM openjdk:8-jdk

# Set the working directory
WORKDIR /app

# Copy the Gradle files
COPY build.gradle settings.gradle /app/

# Copy the source code
COPY app /app/app

# Download and install dependencies
RUN apt-get update && apt-get install -y \
    unzip \
    wget

# Download and install the Android SDK
RUN wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip && \
    unzip android-sdk.zip -d /usr/local/android-sdk && \
    rm -f android-sdk.zip

# Set up environment variables
ENV ANDROID_HOME /usr/local/android-sdk
ENV PATH $PATH:$ANDROID_HOME/tools/bin

# Accept Android SDK licenses
RUN yes | $ANDROID_HOME/tools/bin/sdkmanager --licenses

# Install required Android components
RUN $ANDROID_HOME/tools/bin/sdkmanager "build-tools;28.0.3" "platforms;android-28"

# Build the Android app
RUN ./gradlew assembleDebug

# Expose any necessary ports
EXPOSE 8000

# Define the command to run your application
CMD ["./gradlew", "assembleDebug"]
