#!/bin/sh
VERSION="1.0.5"
GROUPID="net.sf.ezmorph"
ARTIFACTID="ezmorph"
REPO_URL="scp://ssh.sf.net/home/groups/e/ez/ezmorph/htdocs/m2/repo/release"
REPOSITORYID="ezmorph_release"

mkdir build
mvn package source:jar javadoc:jar install
mv target/${ARTIFACTID}-${VERSION}.jar build
mv target/${ARTIFACTID}-${VERSION}-sources.jar build
mv target/${ARTIFACTID}-${VERSION}-javadoc.jar build

mvn deploy:deploy-file -DrepositoryId=${REPOSITORYID} \
    -Durl=${REPO_URL} \
    -DgroupId=${GROUPID} \
    -DartifactId=${ARTIFACTID} \
    -Dversion=${VERSION} \
    -Dpackaging=jar \
    -DgeneratePom=false \
    -Dclassifier=javadoc \
    -Dfile=build/${ARTIFACTID}-${VERSION}-javadoc.jar

mvn deploy:deploy-file -DrepositoryId=${REPOSITORYID} \
    -Durl=${REPO_URL} \
    -DgroupId=${GROUPID} \
    -DartifactId=${ARTIFACTID} \
    -Dversion=${VERSION} \
    -Dpackaging=jar \
    -DgeneratePom=false \
    -Dclassifier=sources \
    -Dfile=build/${ARTIFACTID}-${VERSION}-sources.jar

mvn deploy:deploy-file -DrepositoryId=${REPOSITORYID} \
    -Durl=${REPO_URL} \
    -DgroupId=${GROUPID} \
    -DartifactId=${ARTIFACTID} \
    -Dversion=${VERSION} \
    -Dpackaging=jar \
    -Dfile=build/${ARTIFACTID}-${VERSION}.jar

mvn deploy:deploy-file -DrepositoryId=${REPOSITORYID} \
    -Durl=${REPO_URL} \
    -DgroupId=${GROUPID} \
    -DartifactId=${ARTIFACTID} \
    -Dversion=${VERSION} \
    -Dpackaging=pom \
    -DgeneratePom=false \
    -Dfile=pom.xml
