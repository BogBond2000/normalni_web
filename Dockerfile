FROM bitnami/wildfly:latest
COPY target/web2-1.0-SNAPSHOT.war /opt/bitnami/wildfly/standalone/deployments/
CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]