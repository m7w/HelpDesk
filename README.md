## Helpdesk Application

### Users credentials

**Role 'Employee'**:  
user1_mogilev@yopmail.com/P@ssword1  
user2_mogilev@yopmail.com/P@ssword1

**Role 'Manager'**:  
manager1_mogilev@yopmail.com/P@ssword1  
manager2_mogilev@yopmail.com/P@ssword1

**Role 'Engineer'**:  
engineer1_mogilev@yopmail.com/P@ssword1  
engineer2_mogilev@yopmail.com/P@ssword1

### Back-end

In the folder back-end:  
To test application run **mvn test**.  
To build package run **mvn package**.  
Set the environment variables **SPRING_MAIL_USERNAME** and **SPRING_MAIL_PASSWORD** to some your test mail address to have ability to send notification emails after user actions.  
Run application with **java -jar helpdesk.jar**.  
Back-end starts on _localhost_, port _8080_, context-path _helpdesk_.  
API URL: <http://localhost:8080/helpdesk/api>.  
Authorization URL (to get JWT token): <http://localhost:8080/helpdesk/api/auth/login>.

### Front-end

In the folder front-end:  
To install dependencies run **npm install**  
Run application with **npm start**  
Front-end starts on _localhost_, port _4444_  
Start page <http://localhost:4444>. Login with any above-mentioned user credentials.

### Docker

In the root folder:  
Create .env.sensitive file with content like:  
_SPRING_MAIL_USERNAME=your_mail_address_  
_SPRING_MAIL_PASSWORD=your_mail_password_  
to have ability to send notification emails after user actions.  
Run application with **docker-compose up --build**  
All URLs are the same like in dockerless version.
