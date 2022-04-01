import "./App.css";
import LoginPage from "./components/LoginPage";
import MainPageWithRouter from "./components/MainPage";
import TicketInfo from "./components/TicketInfo";
import TicketCreationPageWithRouter from "./components/TicketCreationPage";
import { useState } from "react";
import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch,
} from "react-router-dom";
import { apiConfigure } from "./services/apiService.js";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  const [isAuthorized, setAuth] = useState(false);
  const [user, setUser] = useState();

  apiConfigure("http://localhost:8080/helpdesk");

  return (
    <Router>
      <Switch>
        <Route exact path="/">
          {isAuthorized ? 
            <Redirect to="/main-page" /> 
            : 
            <LoginPage authCallback={setAuth} userCallback={setUser} />}
        </Route>
          {isAuthorized ?
            <>
              <Route path="/main-page">
                <MainPageWithRouter authCallback={setAuth} />
              </Route>
              <Route exact path="/ticket-info/:ticketId">
                <TicketInfo />
              </Route>
              <ProtectedRoute
                path="/create-ticket"
                condition={user && (user.role === "ROLE_EMPLOYEE" || user.role === "ROLE_MANAGER")}
                component={TicketCreationPageWithRouter}
              />
            </>
              :
              <LoginPage authCallback={setAuth} userCallback={setUser} />
          }
      </Switch>
    </Router>
  );
}

export default App;
