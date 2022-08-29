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
Run application with **java -jar helpdesk.jar**.  
Back-end starts on *localhost*, port *8080*, context-path *helpdesk*.  
API URL: <http://localhost:8080/helpdesk/api>.  
Authorization URL (to get JWT token): <http://localhost:8080/helpdesk/api/auth/login>.  
  
### Front-end  
In the folder front-end:  
To install dependencies run **npm install**  
Run application with **npm start**  
Front-end starts on *localhost*, port *4444*  
Start page <http://localhost:4444>. Login with any above-mentioned user credentials.  
  
### Docker  
In the root folder:  
Run application with **docker-compose up --build**  
All URLs are the same like in dockerless version.  
  
### Heroku  
You can preview running application on [Heroku](https://heroku.com).  
Back-end URL: <https://m7w-helpdesk-backend.herokuapp.com>.  
Front-end URL: <https://m7w-helpdesk-frontend.herokuapp.com>.  
User credentials are the same.  
As on the free plan containers sleeps after 30 mins of inactivity, the first time access may take some time.  
Version for Heroku is on its own branch **deploy-to-heroku**.  
