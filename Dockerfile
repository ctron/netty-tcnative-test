FROM registry.access.redhat.com/ubi7:latest

RUN yum install -y java-1.8.0-openjdk-devel

RUN mkdir maven
WORKDIR /maven
RUN curl -sL https://www-eu.apache.org/dist/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.tar.gz --output maven.tar.gz
RUN tar --strip-components 1 -xaf maven.tar.gz
RUN ls
WORKDIR /
ENV PATH=$PATH:/maven/bin

RUN yum install -y openssl apr

RUN mkdir /build
COPY . /build
WORKDIR /build
RUN mvn -B package && mvn -B org.apache.maven.plugins:maven-dependency-plugin:3.1.1:go-offline
WORKDIR /

CMD [ "mvn", "-f", "/build", "exec:exec" ]
