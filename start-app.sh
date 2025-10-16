#!/bin/bash

export APPDYNAMICS_AGENT_ACCOUNT_ACCESS_KEY=#######
export APPDYNAMICS_AGENT_ACCOUNT_NAME=se-lab
export APPDYNAMICS_CONTROLLER_HOST_NAME=se-lab.saas.appdynamics.com
export APPDYNAMICS_JAVA_AGENT_REUSE_NODE_NAME_PREFIX="acct-vetting-node"
export APPDYNAMICS_AGENT_APPLICATION_NAME=acct-vetting-service
export APPDYNAMICS_AGENT_TIER_NAME=acct-vetting
export APPDYNAMICS_CONTROLLER_PORT=443
export APPDYNAMICS_CONTROLLER_SSL_ENABLED=true
export APPDYNAMICS_AGENT_NODE_NAME="reuse"
export APPDYNAMICS_JAVA_AGENT_REUSE_NODE_NAME="true"
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_SERVICE_NAME=acct-vetting-service
export OTEL_RESOURCE_ATTRIBUTES=service.name=acct-vetting-service,deployment.environment=test-petclinic,service.version=1.0.0,service.namespace=acct-vetting
export AGENT_DEPLOYMENT_MODE=dual

java -javaagent:appdynamics/javaagent.jar -Dagent.deployment.mode=dual -Dotel.instrumentation.jdbc.enabled=true -Dsplunk.profiler.enabled=true -Dsplunk.profiler.memory.enabled=true -Dsplunk.snapshot.profiler.enabled=true -Dsplunk.snapshot.selection.probability=0.2  -Dotel.exporter.otlp.endpoint=http://localhost:4318 -Dappdynamics.sim.enabled=true -jar target/*.jar
