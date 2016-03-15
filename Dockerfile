FROM ivantichy/koncentrator-backend

RUN apt-get update -y -q && \
apt-get install openvpn -y -q && \
apt-get install fail2ban -y -q && \
apt-get install git -y -q && \
apt-get install sudo -y -q && \
rm -rf /var/lib/apt/lists/*


# TODO add user
# mkdir -p /home/java/ && 
RUN cd /home/java/ && git clone \
--branch docker-kernel-3.16 https://github.com/ivantichy/koncentrator-backend-docker.git /home/java && ls

WORKDIR /home/java

RUN chmod +x ./Koncentrator/test/BasicTests/cleanserver.sh

RUN ./Koncentrator/test/BasicTests/cleanserver.sh

COPY ./entrypoint.sh /entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
