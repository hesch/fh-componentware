#!/bin/sh

saveInterface() {
	# $1 name
	# $2 postfix

	fileName="$1$2.java"

	interfaceHead="public interface $1$2"

	imports="import javax.ejb."

	if [ -z $2 ]; then
		imports=""
		interfaceHead="$interfaceHead {"
	else
		interfaceHead="@$2\n$interfaceHead extends $1 {"
		imports="$imports$2;"
	fi
	
	echo "package de.randomerror.chat.interfaces;\n\n$imports\n" >> $fileName
	echo "$interfaceHead" >> $fileName
	echo "\n}" >> $fileName
}


saveInterface $1
saveInterface $1 "Remote"
saveInterface $1 "Local"

