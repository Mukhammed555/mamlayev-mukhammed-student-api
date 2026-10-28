# Jenkins controller image that can run "docker" commands (Docker-capable agent)
# Author: Mukhammed Mamlayev
FROM jenkins/jenkins:lts-jdk21

USER root
# Docker CLI + buildx/compose plugins only. The Docker daemon of the host is
# used through the mounted socket /var/run/docker.sock (see docker-compose.jenkins.yml).
COPY --from=docker:cli /usr/local/bin/docker /usr/local/bin/docker
COPY --from=docker:cli /usr/local/libexec/docker/cli-plugins /usr/local/libexec/docker/cli-plugins

# Pre-install the plugins the pipeline needs (git is already in the base image)
RUN jenkins-plugin-cli --plugins \
    workflow-aggregator git github junit credentials-binding pipeline-stage-view
