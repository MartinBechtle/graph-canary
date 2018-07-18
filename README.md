# graph-canary
Dashboard for canary endpoints

## Running / Deploying

### Running with docker

Clone to your hard drive:

	git clone https://github.com/MartinBechtle/graph-canary
		
In your deployment pipeline you should override the configuration in

	src/main/resources
	
If you need to run in multiple environments, you can create multiple files in such folder, by choosing a profile name for each.
For example, you could create the following files:

	src/main/resources/application-dev.yml
	src/main/resources/application-prod.yml
	
Note: do not use a profile called 'test' as it will mess up with the application's bootstrap.
In fact, such profile is used for unit and integration tests in this project.

Finally, you can build a docker image that will contain your custom configuration:

	docker build . -t martinb86/graphcanary:latest 
	
Feel free to customise the image name and tag in a way that best suits your organisation. 
Once the image is uploaded to your registry, you can run with different profiles depending on the environment where you deploy.

	docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 martinb86/graphcanary:latest
	
### Running natively

Steps:
* Make sure you have JRE 8
* Customise application.yaml 
* Run with IntelliJ as a Spring Boot application, or run with the embedded gradle dist:

	./gradlew bootRun


## Setting up email

### Enabling

To enable email notifications, just specify the recipient in your application.yaml

	canary:
	 email:
	  to: vash1986@gmail.com
	  from: martin.bechtle@tide.co

### Configuring

Here is an example of setting up in your application.yaml with Amazon SES (simple email service)

	 mail:
      host: email-smtp.us-west-2.amazonaws.com
      username: username
      password: password
      properties:
        mail:
          transport:
            protocol: smtp
          smtp:
            port: 25
            auth: true
            starttls:
              enable: true
              required: true
              
For further examples, such as using with Gmail, please refer to Spring's documentation or the guide on Baeldung:
* http://www.baeldung.com/spring-email
* https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-email.html

## Troubleshooting

If weird duplication of services occurs, check your configuration. 
When configuring the canary endpoints, make sure that you give them the correct name, IE it needs to correspond to the
service name that the canary endpoint returns.