#!/bin/bash
# this is config for daily build

source /etc/profile

SELF=$(cd $(dirname $0); pwd -P)/$(basename $0)
CURRENTDIR=$(cd $(dirname $0); pwd -P)
DATE=$(date +%y-%m-%d-%H-%M)
APP_SOURCE_ROOTDIR=/home/git/data_monitor
artifactIdLine=$(cat $APP_SOURCE_ROOTDIR/pom.xml | grep "artifactId" | head -n 1 | awk -F artifactId  '{print $2}')
ARTIFACTID=${artifactIdLine#>}
ARTIFACTID=${artifactIdLine%</}
versionLine=$(cat $APP_SOURCE_ROOTDIR/pom.xml | grep "version" | head -n 1 | awk -F version  '{print $2}')
VERSION=${VERSION#>}
VERSION=${VERSION%</}
APP_NAME=$ARTIFACTID-$VERSION
echo "APP_NAME:$APP_NAME"
APP_BUILD_DIR=$CURRENTDIR/build
APP_BUILD_LOGFILE=$CURRENTDIR/log/log.log
GIT_URL=https://github.com/lwfwind/data_monitor.git
rm -rf $CURRENTDIR/log/
rm -rf $APP_BUILD_DIR
test -d $CURRENTDIR/log || mkdir -p $CURRENTDIR/log
test -f $APP_BUILD_LOGFILE || touch $APP_BUILD_LOGFILE
test -d $APP_BUILD_DIR || mkdir -p $APP_BUILD_DIR

do_pre() {
	echo "do pre start at $(date)" | tee -a $APP_BUILD_LOGFILE
	test -d $APP_SOURCE_ROOTDIR || mkdir -p $APP_SOURCE_ROOTDIR
	cd $APP_SOURCE_ROOTDIR/
	rm -rf $APP_BUILD_DIR/* | tee -a $APP_BUILD_LOGFILE
	rm -rf $CURRENTDIR/log/*.*
	echo "do pre end at $(date)" | tee -a $APP_BUILD_LOGFILE
}


do_git() {
	echo "do git start at $(date)" | tee -a $APP_BUILD_LOGFILE
	cd $APP_SOURCE_ROOTDIR
	branch=$*
	currentBranch=`git branch`
	if [ "$currentBranch" = "* $branch" ]; then
		echo "do git pull at $(date)" | tee -a $APP_BUILD_LOGFILE
		git pull --recurse-submodules 2>&1 | tee -a $APP_BUILD_LOGFILE
	else
		cd ..
		rm -rf $APP_SOURCE_ROOTDIR
		git clone --recursive $GIT_URL -b $branch 2>&1 | tee -a $APP_BUILD_LOGFILE
		echo "do git clone at $(date)" | tee -a $APP_BUILD_LOGFILE
	fi
	echo "do git end at $(date)" | tee -a $APP_BUILD_LOGFILE
}

do_build() {
	echo "do build start at $(date)" | tee -a $APP_BUILD_LOGFILE
	cd $APP_SOURCE_ROOTDIR
	./mvnw clean package -Dmaven.test.skip=true 2>&1 | tee -a $APP_BUILD_LOGFILE
	echo "do build end at $(date)" | tee -a $APP_BUILD_LOGFILE
}

do_sync() {
	echo "do sync start at $(date)" | tee -a $APP_BUILD_LOGFILE
	cp $APP_SOURCE_ROOTDIR/target/$APP_NAME.jar $APP_BUILD_DIR/$APP_NAME.jar
	cp $APP_SOURCE_ROOTDIR/src/main/resources/application.properties APP_BUILD_DIR/application.properties
	test -f $APP_BUILD_DIR/$APP_NAME.jar && echo "<br>Build Successfully"
	test -f $APP_BUILD_DIR/$APP_NAME.jar || echo "<br>Build Failed"
	test -f $APP_BUILD_DIR/$APP_NAME.jar || exit 1
	echo "do sync end at $(date)" | tee -a $APP_BUILD_LOGFILE
}

do_start() {
	echo "do launch start at $(date)" | tee -a $APP_BUILD_LOGFILE
	config=$*
	cd $APP_BUILD_DIR
	rm -f pid
	nohup java -jar $APP_NAME.jar $config > /dev/null 2>&1 &
	echo $! > pid
	echo "start success"
	echo "do launch end at $(date)" | tee -a $APP_BUILD_LOGFILE
}

do_stop() {
	echo "do stop start at $(date)" | tee -a $APP_BUILD_LOGFILE
	pid=`ps -ef | grep $APP_NAME | grep -v grep | grep -v kill | awk '{print $2}'`
	if [ ${pid} ]; then
		echo 'stop process...'
		kill -15 $pid
	fi
	sleep 5
	pid=`ps -ef | grep $APP_NAME | grep -v grep | grep -v kill | awk '{print $2}'`
	if [ ${pid} ]; then
		echo 'kill process'
		kill -9 $pid
	else
		echo 'stop success'
	fi
	echo "do stop end at $(date)" | tee -a $APP_BUILD_LOGFILE
}

do_check() {
	echo "do check start at $(date)" | tee -a $APP_BUILD_LOGFILE
	pid=`ps -ef | grep $APP_NAME | grep -v grep | grep -v kill | awk '{print $2}'`
	if [ ${pid} ]; then
		echo "$APP_NAME is running"
	else
		echo "$APP_NAME is NOT running"
	fi
	echo "do check end at $(date)" | tee -a $APP_BUILD_LOGFILE
}

#the main
main() {
	if ps -C sh -o %a | grep $SELF 2>&1 > /dev/null; then
   		echo "another $SELF is running"
   		exit -1
	fi
	
	if [ $# -eq 2 ]; then
		local all_parameters=$*
		echo "--> parameters:$all_parameters" | tee -a $APP_BUILD_LOGFILE
		arr=($@)
		OPERATION=${arr[0]}
		BRANCH=${arr[1]}
	else if [ $# -eq 1 ]; then
		local all_parameters=$*
		echo "--> parameters:$all_parameters" | tee -a $APP_BUILD_LOGFILE
		arr=($@)
		OPERATION=${arr[0]}
		BRANCH=dev
	else if [ $# -eq 0 ]; then
		OPERATION="all"
		echo "--> parameters:all" | tee -a $APP_BUILD_LOGFILE
		BRANCH=dev
	else
		echo "invalid parameters" | tee -a $APP_BUILD_LOGFILE
		exit -1
	fi
	
	for op in $OPERATION
	do
		echo "--> doing operation:" $op | tee -a $LOGFILE
		case "$op" in
			'build')
				do_pre
				do_git $BRANCH
				do_build 
				do_sync
			;;
			
			'start')
				do_start
			;;
			
			'stop')
				do_stop
			;;
			
			'status')
				do_check
			;;
			
			'all')
				do_pre
				do_git $BRANCH
				do_build 
				do_sync
				do_start
			;;
			
			*)
				echo "unsupported operation"
			;;
		esac
	done
}

main $*
