import React, { useState, useEffect } from "react";
import TabPanel from "./TabPanel";
import TicketsTable from "./TicketsTable";
import { AppBar, Button, Tab, Tabs } from "@material-ui/core";
import { Link, Switch, Route } from "react-router-dom";
import { useHistory, withRouter } from "react-router";
import TicketInfoWithRouter from "./TicketInfo";
import ticketService from "../services/ticketService";
import { useDebouncedEffect } from "../components/DebouncedEffect";

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}

function MainPage(props) {
  const [tabValue, setTabValue] = useState(0);
  const [tickets, setTickets] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [orderBy, setOrderBy] = useState();
  const [order, setOrder] = useState();
  const [ticketsCount, setTicketsCount] = useState(0);
  const [searchColumn, setSearchColumn] = useState("t.name");
  const [searchString, setSearchString] = useState();
  const [searchError, setSearchError] = useState();
  const [actionClicked, setActionClicked] = useState(true);

  const debouncedSearchString = useDebouncedEffect(searchString, 1000);

  useEffect(() => {
    // put requests for tickets here
    const params = {
      is_my: tabValue,
      page: page,
      size: rowsPerPage,
      order_by: orderBy,
      order: order,
      searchColumn: searchColumn,
      searchString: debouncedSearchString,
    };

    ticketService
      .getTickets(params)
      .then((response) => {
        setSearchError();
        setTicketsCount(response.data.count);
        setTickets(response.data.entities);
      })
      .catch((error) => {
        switch (error.response.status) {
          case 400:
            setSearchError(error.response.data["getTicketsByUser.searchParams"]);
            break;
          default:
            break;
        }
      });
  }, [
    tabValue,
    orderBy,
    order,
    page,
    rowsPerPage,
    searchColumn,
    debouncedSearchString,
    actionClicked,
  ]);

  const handleCreate = () => {};

  const history = useHistory();

  const handleLogout = () => {
    // put logout logic here
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    props.authCallback(false);
    history.push("/");
  };

  const handleTabChange = (event, value) => {
    setTabValue(value);
  };

  const handlePagination = (page, rowsPerPage) => {
    setPage(page);
    setRowsPerPage(rowsPerPage);
  };

  const handleSort = (newOrderBy) => {
    if (newOrderBy === orderBy) {
      setOrder(order === "asc" ? "desc" : "asc");
    } else {
      setOrderBy(newOrderBy);
      setOrder("asc");
    }
  };

  const handleSearchTicket = (searchColumn, searchString) => {
    // put search request here

    setSearchColumn(searchColumn);
    setSearchString(searchString);
    setPage(0);
  };

  const handleSelectAction = () => {
    setActionClicked(actionClicked ? false : true);
  };

  const { path } = props.match;
  const user = JSON.parse(localStorage.getItem("user"));

  return (
    <>
      <Switch>
        <Route exact path={path}>
          <div className="buttons-container">
            {user.role !== "ROLE_ENGINEER" ? (
              <Button
                component={Link}
                to="/create-ticket"
                onClick={handleCreate}
                variant="contained"
                color="primary"
              >
                Create Ticket
              </Button>
            ) : (
              <span></span>
            )}
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
              <Tabs variant="fullWidth" onChange={handleTabChange} value={tabValue}>
                <Tab label="All tickets" {...a11yProps(0)} />
                <Tab label="My tickets" {...a11yProps(1)} />
              </Tabs>
              <TabPanel value={tabValue} index={0}>
                <TicketsTable
                  searchCallback={handleSearchTicket}
                  searchErrorMessage={searchError}
                  tickets={tickets}
                  sortCallback={handleSort}
                  orderBy={orderBy}
                  order={order}
                  paginationCallback={handlePagination}
                  ticketsCount={ticketsCount}
                  selectActionCallback={handleSelectAction}
                />
              </TabPanel>
              <TabPanel value={tabValue} index={1}>
                <TicketsTable
                  searchCallback={handleSearchTicket}
                  tickets={tickets}
                  sortCallback={handleSort}
                  orderBy={orderBy}
                  order={order}
                  paginationCallback={handlePagination}
                  ticketsCount={ticketsCount}
                  selectActionCallback={handleSelectAction}
                />
              </TabPanel>
            </AppBar>
          </div>
        </Route>
        <Route path={`${path}/:ticketId`}>
          <TicketInfoWithRouter onAction={handleSelectAction} />
        </Route>
      </Switch>
    </>
  );
}

const MainPageWithRouter = withRouter(MainPage);
export default MainPageWithRouter;
