#!/bin/bash
export APPDYNAMICS_AGENT_ACCOUNT_ACCESS_KEY=<YOUR_TOKEN_HERE>
export APPDYNAMICS_AGENT_ACCOUNT_NAME=<YOUR_APPD_ACCOUNT_NAME_HERE>
export APPDYNAMICS_CONTROLLER_HOST_NAME=<YOUR_APPD_URL_HERE>
export APPDYNAMICS_CONTROLLER_PORT=443
export APPDYNAMICS_CONTROLLER_SSL_ENABLED=true
export APPDYNAMICS_AGENT_APPLICATION_NAME=petclinic
export APPDYNAMICS_AGENT_TIER_NAME=petclinic-tier
export APPDYNAMICS_AGENT_NODE_NAME="reuse"
export APPDYNAMICS_JAVA_AGENT_REUSE_NODE_NAME="true"
export APPDYNAMICS_JAVA_AGENT_REUSE_NODE_NAME_PREFIX="petclinic-node"

java -javaagent:appdynamics/javaagent.jar -Dappdynamics.opentelemetry.enabled=true -Dotel.traces.exporter=otlp -Dotel.exporter.otlp.traces.endpoint=http://0.0.0.0:4317 -Dotel.resource.attributes="service.name=petclinic-tier,service.namespace=petclinic,deployment.environment=petclinic-ubuntu" -jar target/*.jar
