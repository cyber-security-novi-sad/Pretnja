
mkdir temp
cd temp
xjc -p com.eds.EDS_Library.diagram "%~f1"
javac com/eds/EDS_Library/diagram/*.java
jar cfvM ../lib/model_lib.jar com/eds/EDS_Library/diagram/*.class
cd ..
rd /s /q temp