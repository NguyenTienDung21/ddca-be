FROM openjdk:17
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY ./build/libs/* ./usr/app/
CMD ["java","-jar","./usr/app/DDCA-BE-0.0.1-SNAPSHOT.jar"]