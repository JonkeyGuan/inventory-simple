cd ../
mvn clean package -DskipTests

cd env
oc start-build inventory-simple --from-dir=../target/ -F
