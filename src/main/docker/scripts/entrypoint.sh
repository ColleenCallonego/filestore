#!/bin/bash

FILESTORE_AUTH_URI=${FILESTORE_AUTH_URI:-http://127.0.0.1:8080}
FILESTORE_HOME=${FILESTORE_HOME:-/opt/jboss/filestore}

cp -a /opt/jboss/config/filestore-template.xml /opt/jboss/wildfly/standalone/configuration/filestore.xml

sed -i "s|##FILESTORE_HOME##|$FILESTORE_HOME|" /opt/jboss/wildfly/standalone/configuration/filestore.xml
sed -i "s|##FILESTORE_AUTH_URI##|$FILESTORE_AUTH_URI|" /opt/jboss/wildfly/standalone/configuration/filestore.xml

mkdir -p $FILESTORE_HOME

/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -c filestore.xml
