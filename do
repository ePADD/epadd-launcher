source ~/.bashrc
set -e
set -u
mvn clean compile assembly:single
jar uvf target/epadd-standalone-jar-with-dependencies.jar splash-image.png

cd lib
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar muse-icon.png
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar edu/stanford/ejalbert/launching/windows/windowsConfig.properties 
cp ../target/epadd-standalone-jar-with-dependencies.jar ../target/epadd-discovery-standalone-jar-with-dependencies.jar 

jar uvf ../target/epadd-discovery-standalone-jar-with-dependencies.jar edu/stanford/ejalbert/launching/windows/windowsConfig.properties 

cd ../../epadd/lib
jar uvf ../../epadd-launcher/target/epadd-standalone-jar-with-dependencies.jar crossdomain.xml

cd ../target
/bin/cp -p epadd-0.0.1-SNAPSHOT.war epadd.war
jar uvf ../../epadd-launcher/target/epadd-standalone-jar-with-dependencies.jar epadd.war

/bin/cp -p epadd-discovery-0.0.1-SNAPSHOT.war epadd.war
jar uvf ../../epadd-launcher/target/epadd-discovery-standalone-jar-with-dependencies.jar epadd.war

cd ../../epadd-launcher
/bin/mv -f target/epadd-standalone-jar-with-dependencies.jar epadd-standalone.jar
/bin/mv -f target/epadd-discovery-standalone-jar-with-dependencies.jar epadd-discovery-standalone.jar
ant
