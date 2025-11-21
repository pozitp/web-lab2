FROM quay.io/wildfly/wildfly:38.0.0.Final
COPY build/libs/web-lab2.war /opt/jboss/wildfly/standalone/deployments/web-lab2.war
EXPOSE 8080
