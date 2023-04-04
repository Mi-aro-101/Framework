# Initializing variable
tomcat=~/apache-tomcat-10.0.27/
var=$(pwd)
basename $(pwd)
mydir="$(basename $PWD)"

# Beginning Operation
cd ./Framework
javac -d . *.java
mkdir -p ../WEB-INF/lib
jar -cf ../WEB-INF/lib/liantsiky.jar etu2020 && cp -r etu2020 ../src
cd ../src
find -name "*.java" > sources.txt
javac -d . @sources.txt
mkdir -p ../WEB-INF/classes
cp -r --parent `ls | egrep -v 'etu2020'` ../WEB-INF/classes && find ../WEB-INF/classes -type f -name "*.java" -delete
cd ../
cp ./Framework/web.xml ./WEB-INF

jar -cf $tomcat/webapps/$mydir.war WEB-INF
cd ./web_page
jar uf $tomcat/webapps/$mydir.war *
cd $tomcat/bin && ./shutdown.sh && ./startup.sh