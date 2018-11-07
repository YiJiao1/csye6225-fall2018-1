#!/bin/bash 



aws cloudformation describe-stacks --stack-name  MyCICD| grep OutputValue>> /tmp/leocicd.log

codedeployCIsecretKey=$(cat /tmp/leocicd.log|sed -n "1 p"| cut -d'"' -f4)
lambdaExecuationRole=$(cat /tmp/leocicd.log|sed -n "2 p"| cut -d'"' -f4)
lambdaCIaccesskey=$(cat /tmp/leocicd.log|sed -n "3 p"|cut -d'"' -f4)
lambdaCIsecretkey=$(cat /tmp/leocicd.log|sed -n "4 p"|cut -d'"' -f4)
instanceProfile=$(cat /tmp/leocicd.log|sed -n "5 p"|cut -d'"' -f4)
codedeployCIaccessKey=$(cat /tmp/leocicd.log|sed -n "6 p"|cut -d'"' -f4)


echo "
codeployCIUser:
$codedeployCIaccessKey
$codedeployCIsecretKey

lambdaCIUser:
$lambdaCIaccesskey
$lambdaCIsecretkey

lambdaExecuationRoleARN:
$lambdaExecuationRole
"
rm /tmp/leocicd.log
