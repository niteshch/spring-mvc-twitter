# REST API for manipulating Twitter data
The project is intended to demonstrate building RESTful services using Spring MVC. In this project, we will be interacting with twitter data in MySQL database.

# Twitter REST API using Spring MVC
Following operations are exposed by this service:
1. An endpoint to read the tweets for a given user (includes tweets of the user and the people being followed by the user). An extra “search=” argument can be used to further filter tweets based on keyword.
2. Endpoints to get the list of people a user is following as well as the followers of the user.
3. An endpoint to start following another user.
4. An endpoint to unfollow another user.

# Setup
- Use the `twitter_output.sql` file to import the MySQL dump of twitter data.

- All the requests to the service must be authenticated. Change the `username` and `password` in `spring-servlet.xml` file.

```
<security:authentication-manager>
     <security:authentication-provider>
	 <security:user-service>
	      <security:user name="john" password="doe" authorities="ROLE_USER, ROLE_ADMIN" />
	      </security:user-service>
     </security:authentication-provider>
</security:authentication-manager>
```
  
- Update the database details in the `spring-servlet.xml` before deploying the service.
  
```
<bean id="dataSource"
	class="org.springframework.jdbc.datasource.DriverManagerDataSource">
	<property name="driverClassName" value="com.mysql.jdbc.Driver" />
	<property name="url" value="jdbc:mysql://localhost:3306/twitter" />
	<property name="username" value="root" />
	<property name="password" value="root" />
</bean>
```

- Build the project using Maven.

- Deploy the generated war file to the tomcat server.

- Once deployed, the details of API can be found at [http://<server_url>/<context_root>/swagger-ui.html]([http://<server_url>/<context_root>/swagger-ui.html])
