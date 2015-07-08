#!/bin/bash -u -e
source ~/.bashrc

# build flattened jar
mvn # clean compile assembly:single

# add the needed icons etc.
cd lib
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar splash-image.png muse-icon.png edu/stanford/ejalbert/launching/windows/windowsConfig.properties 

# make a replica of what we have so far, for discovery
cp ../target/epadd-standalone-jar-with-dependencies.jar ../target/epadd-discovery-standalone-jar-with-dependencies.jar 

# add crossdomain.xml only to full epadd, not to discovery
jar uvf ../target/epadd-standalone-jar-with-dependencies.jar crossdomain.xml

# add wars to standalone-jar
cd ../../epadd/target
/bin/cp -p epadd-0.0.1-SNAPSHOT.war epadd.war
jar uvf ../../epadd-launcher/target/epadd-standalone-jar-with-dependencies.jar epadd.war

# add discovery war to discovery standalone
/bin/cp -p epadd-discovery-0.0.1-SNAPSHOT.war epadd.war
jar uvf ../../epadd-launcher/target/epadd-discovery-standalone-jar-with-dependencies.jar epadd.war
# delete epadd.war to avoid any confusion about which version it is (regular or discovery)
/bin/rm -f epadd.war

# prepare standalone jars
cd ../../epadd-launcher
/bin/mv -f target/epadd-standalone-jar-with-dependencies.jar epadd-standalone.jar
/bin/mv -f target/epadd-discovery-standalone-jar-with-dependencies.jar epadd-discovery-standalone.jar

# package into exe and dmg
ant
