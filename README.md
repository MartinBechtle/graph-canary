# graph-canary
Dashboard for canary endpoints


## Setting up email

### Enabling

To enable email notifications, just specify the recipient in your application.yaml

	canary:
      mailrecipient: your-address@email.com

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
http://www.baeldung.com/spring-email
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-email.html