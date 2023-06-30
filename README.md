# demo
Coopeuch demo backend

Como desplegar
1. Se incluye script de base datos, pero de todas formas está configurado para que Spring cree la base datos al iniciar el server
2. ./mvnw install    
3. Asumiendo que las base datos esta en localhost con usuario root
java -DMYSQL_PASS="password" -jar target/demo-0.0.1-SNAPSHOT.jar
