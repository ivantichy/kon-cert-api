#!/bin/bash

if [ "$1" == "bash" ]; then
	"$@"
	exit
fi 

if [[ ! -f haveca ]]; then

        echo "y"> haveca
	Koncentrator/test/BasicTests/cleanserver.sh

fi

java -classpath Koncentrator/*:Koncentrator/lib/* cz.ivantichy.httpapi.handlers.vpnapi.RunnerCERT >> cert.log


