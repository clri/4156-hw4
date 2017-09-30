README
CodeHolic: Caroline Roig-Irwin/Jeongmin Oh


Project taken from base source [3], slightly edited to add a couple of front-end
html pages. 

REQUIREMENTS:
maven--10.11.6 or later
mysql--5.7 or later
spring--will be automatically downloaded thru maven

HOW TO RUN:
1. Change the line "spring.jpa.hibernate.ddl-auto=none" to "spring.jpa.hibernate.ddl-auto=create" in ui/src/main/resources/application.properties
2. Start MySQL server. Execute pre-exec.sql; this will create your database.
3. On the command line, inside the ui directory, run "./mvnw spring-boot:run"
4. Once the server has started successfully, kill it.
5. Your table should be inside the database db_example. Use MySQL to execute post-create.sql
6. Revert the line in application.properties to none instead of create (this is important so that the data does not get dropped and recreated).
7. run "./mvnw spring-boot:run" and go to localhost:8080 in the browser; now you can input a name and email into the form and add items to your database as well as viewing it as JSON.

WORKS CITED

1. https://stackoverflow.com/questions/42115370/how-to-pass-html-input-values-into-database-from-form-springboot
2.https://stackoverflow.com/questions/9311940/redirect-to-dynamic-url-in-spring-mvc
3. https://spring.io/guides/gs/accessing-data-mysql/
4. https://spring.io/guides/gs/serving-web-content/
5. https://stackoverflow.com/questions/43574426/how-to-resolve-java-lang-noclassdeffounderror-javax-xml-bind-jaxbexception-in-j