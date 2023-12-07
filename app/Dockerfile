# Use an official Android runtime as a parent image
FROM openjdk:8-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Set environment variables
ENV ANDROID_HOME /usr/local/android-sdk
ENV PATH $PATH:$ANDROID_HOME/tools/bin

# Install required dependencies
RUN apt-get update -qq \
    && apt-get install -y wget unzip \
    && wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip \
    && unzip android-sdk.zip -d $ANDROID_HOME \
    && rm -f android-sdk.zip \
    && yes | $ANDROID_HOME/tools/bin/sdkmanager --licenses

# Your build command (modify as needed)
CMD ["./gradlew", "assembleDebug"]