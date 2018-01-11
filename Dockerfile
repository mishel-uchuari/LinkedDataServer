FROM mlaccetti/docker-oracle-java8-ubuntu-16.04
MAINTAINER Mikel Egaña Aranguren <mikel.egana.aranguren@gmail.com>

WORKDIR /opt
RUN wget http://ftp.cixug.es/apache/tomcat/tomcat-8/v8.0.48/bin/apache-tomcat-8.0.48.tar.gz
RUN tar -xvzf apache-tomcat-8.0.48.tar.gz
COPY ROOT.war /opt/apache-tomcat-8.0.48/webapps/ROOT.war

CMD /opt/apache-tomcat-8.0.48/bin/catalina.sh run 

EXPOSE 8080