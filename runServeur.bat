:: maitre a joure le repo git local
git pull
:: supprime l'encien jar
call mvn clean
:: compile le projet
call mvn package -DskipTests
:: execute le projet
pause

java -jar target/Backend-jar-with-dependencies.jar

