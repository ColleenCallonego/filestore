FROM jboss/wildfly:25.0.0.Final

USER root

ADD scripts /opt/jboss/scripts
ADD templates /opt/jboss/config
ADD blanchard.filestore-${project.version}.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

RUN chmod +x /opt/jboss/scripts/entrypoint.sh && chown -R jboss:jboss /opt/jboss

USER jboss

EXPOSE 8080

ENTRYPOINT ["sh", "/opt/jboss/scripts/entrypoint.sh"]
