# real-world-crud
Real World Crud

Some of the articles referred to during this POC can be found in code comments.<br/>

Run the application<br/>
go to http://localhost:8080<br/>
log in with credentials user:password (default user and its schema is generated by spring security) <br/>
you will be redirected to swagger ui<br/>

- create a customer<br/>
- upload a file for a customer using upload endpoint created for quick demonstration<br/>
- get a list of files uploaded using get /files <br/>
- download the file using the url returned from the request with the file id<br/>
- crud operations are provided for files and customers<br/>

You can access the database through http://localhost:8080/h2-console/ with sa:password


