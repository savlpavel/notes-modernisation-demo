# README #

This is a simple demo application to show how a Vaadin application can be connected to an existing Notes application.

Not for production use, this is only a demo without error handling and without security.

A detailed step-by-step tutorial can be found here: http://stephankopp.net/notes-with-vaadin-tutorial/ 

### How do I get set up? ###

1. Download repository
2. Copy the tasks.nsf to a Domino server
3. Sign the database
4. Change url (at two methods) in the DatabaseConnector.java file to your server and filepath
5. Use maven command "mvn install spring-boot:run" to run the application
6. Use your browser: http://localhost:8080/

### Who do I talk to? ###

* Repo owner: Stephan Kopp
* More details and contact information: http://stephankopp.net