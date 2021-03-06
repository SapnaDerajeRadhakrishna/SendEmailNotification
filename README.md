The sample **Email Notification** application sends an email notification from configured *gmail* address to any configured email address not restricted
to gmail.

**To start the application, from the terminal**

1. Using gradle as a build tool: 
`./gradlew bootRun`

2. Using the jar *SendEmailNotification-0.0.1-SNAPSHOT.jar* available in _build/libs_ directory , when you compile the application using the command `./gradlew clean build`


**The following ‘curl’ command can be used to trigger the email notification:-**

`curl -v -d '{"toAddress”:”abc@gmail.com","subject":"test","text":"test email from application"}' -H "Content-Type: application/json" -POST http://localhost:8080/postNotification`

**How to encrypt the password**

In order to send the email, we need to provide the `from email address` and the `password` to the application, specify it in `application.properties`  in the `src\main\resources`.
Use the class `AESEncryption.java` in the source code to encrypt the password. 

_Please be kind enough not to commit the changes made to the `AESEncryption.java` when you try to generate the encrypted password_
