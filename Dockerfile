FROM mlaccetti/docker-oracle-java8-ubuntu-16.04
MAINTAINER Mikel Egaña Aranguren <mikel.egana.aranguren@gmail.com>

WORKDIR /opt
RUN wget http://ftp.cixug.es/apache/tomcat/tomcat-8/v8.0.48/bin/apache-tomcat-8.0.48.tar.gz
RUN tar -xvzf apache-tomcat-8.0.48.tar.gz
RUN rm apache-tomcat-8.0.48.tar.gz
RUN rm -rf apache-tomcat-8.0.48/webapps/ROOT
COPY target/ROOT.war apache-tomcat-8.0.48/webapps/ROOT.war

CMD apache-tomcat-8.0.48/bin/catalina.sh run 

EXPOSE 8080
