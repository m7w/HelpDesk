import React, { useState, useEffect } from "react";
import TabPanel from "./TabPanel";
import TicketsTable from "./TicketsTable";
import { AppBar, Button, Tab, Tabs } from "@material-ui/core";
import { Link, Switch, Route } from "react-router-dom";
import { useHistory, withRouter } from "react-router";
import TicketInfoWithRouter from "./TicketInfo";
import { ALL_TICKETS, MY_TICKETS } from "../constants/mockTickets";
import apiService from "../services/apiService.js";

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}

function MainPage(props) {

  const [tabValue, setTabValue] = useState(0);
  const [myTickets, setMyTickets] = useState(MY_TICKETS);
  const [allTickets, setAllTickets] = useState(ALL_TICKETS);
  const [filteredTickets, setFilteredTickets] = useState([]);

  useEffect(()=> {
    // put requests for tickets here
    apiService.get("/tickets/employee");
  }, []); 

  const handleCreate = () => {
  };

  const history = useHistory();

  const handleLogout = () => {
    // put logout logic here
    localStorage.removeItem("token");
    props.authCallback(false);
    history.push("/");
  };

  const handleTabChange = (event, value) => {
      setTabValue(value);
      setFilteredTickets([]);
  };

  const handleSearchTicket = (event) => {
    // put search request here

    //const { tabValue, myTickets, allTickets } = this.state;

    if (tabValue === 0) {
      const filteredTickets = myTickets.filter((ticket) =>
        ticket.name.includes(event.target.value.toLowerCase())
      );

      setFilteredTickets(filteredTickets);
    }

    if (tabValue === 1) {
      const filteredTickets = allTickets.filter((ticket) =>
        ticket.name.includes(event.target.value.toLowerCase())
      );

      setFilteredTickets(filteredTickets);
    }
  };

  const { path } = props.match;

  return (
    <>
      <Switch>
        <Route exact path={path}>
          <div className="buttons-container">
            <Button
              component={Link}
              to="/create-ticket"
              onClick={handleCreate}
              variant="contained"
              color="primary"
            >
              Create Ticket
            </Button>
            <Button
              component={Link}
              to="/"
              onClick={handleLogout}
              variant="contained"
              color="secondary"
            >
              Logout
            </Button>
          </div>
          <div className="table-container">
            <AppBar position="static">
              <Tabs
                variant="fullWidth"
                onChange={handleTabChange}
                value={tabValue}
              >
                <Tab label="My tickets" {...a11yProps(0)} />
                <Tab label="All tickets" {...a11yProps(1)} />
              </Tabs>
              <TabPanel value={tabValue} index={0}>
                <TicketsTable
                  searchCallback={handleSearchTicket}
                  tickets={
                    filteredTickets.length ? filteredTickets : myTickets
                  }
                />
              </TabPanel>
              <TabPanel value={tabValue} index={1}>
                <TicketsTable
                  searchCallback={handleSearchTicket}
                  tickets={
                    filteredTickets.length ? filteredTickets : allTickets
                  }
                />
              </TabPanel>
            </AppBar>
          </div>
        </Route>
        <Route path={`${path}/:ticketId`}>
          <TicketInfoWithRouter />
        </Route>
      </Switch>
    </>
  );
}

const MainPageWithRouter = withRouter(MainPage);
export default MainPageWithRouter;
