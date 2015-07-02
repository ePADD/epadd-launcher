source ~/.bashrc
set -u
mvn clean compile assembly:single

cd lib
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar splash-image.png
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar muse-icon.png
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar edu/stanford/ejalbert/launching/windows/windowsConfig.properties 
cp ../target/epadd-standalone-jar-with-dependencies.jar ../target/epadd-discovery-standalone-jar-with-dependencies.jar 

jar uvf ../target/epadd-discovery-standalone-jar-with-dependencies.jar edu/stanford/ejalbert/launching/windows/windowsConfig.properties 

cd ../target
/bin/cp -p epadd-0.0.1-SNAPSHOT.war epadd.war
jar uvf ../../epadd-launcher/target/epadd-standalone-jar-with-dependencies.jar epadd.war

# add crossdomain.xml only to full epadd, not to discovery
cd ../lib
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar crossdomain.xml
/bin/cp -p epadd-discovery-0.0.1-SNAPSHOT.war epadd.war
jar uvf ../../epadd-launcher/target/epadd-discovery-standalone-jar-with-dependencies.jar epadd.war

cd ../../epadd-launcher
/bin/mv -f target/epadd-standalone-jar-with-dependencies.jar epadd-standalone.jar
/bin/mv -f target/epadd-discovery-standalone-jar-with-dependencies.jar epadd-discovery-standalone.jar
ant
