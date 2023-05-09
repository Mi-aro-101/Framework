# Initializing variable
tomcat=~/apache-tomcat-10.0.27/
var=$(pwd)
basename $(pwd)
mydir="$(basename $PWD)"

# Beginning Operation
cd ./Framework

# Compile my framework and extract it to the 'lib' directory
javac -d . *.java
mkdir -p ../WEB-INF/lib
jar -cf ../WEB-INF/lib/miaro.jar etu2020 && cp -r etu2020 ../src

# Compile src java's of the user and move '.class' them to the 'classes' directory
cd ../src                                                   
find -name "*.java" > sources.txt
javac -d . @sources.txt
mkdir -p ../WEB-INF/classes
cp -r --parent `ls | egrep -v 'etu2020'` ../WEB-INF/classes && find ../WEB-INF/classes -type f -name "*.java" -delete
cd ../

# Move the web.xml to the WEB-INF directory
cp ./Framework/web.xml ./WEB-INF

# 'war'ing the entire projec and deploy it to tomcat
jar -cf $tomcat/webapps/$mydir.war WEB-INF
cd ./web_page
jar uf $tomcat/webapps/$mydir.war *
cd $tomcat/bin && ./shutdown.sh && ./startup.sh