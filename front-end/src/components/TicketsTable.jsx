import React, { useState } from "react";
import PropTypes from "prop-types";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Tooltip,
  TableSortLabel,
  useMediaQuery,
} from "@material-ui/core";
import { Link } from "react-router-dom";
import { withRouter } from "react-router";
import { TICKETS_TABLE_COLUMNS } from "../constants/tablesColumns";
import { STATUSES } from "../constants/inputsValues";
import DropDown from "./DropDown";
import SplitButton from "./SplitButton";
import TablePaginationActions from "./TablePaginationActions";
import ticketService from "../services/ticketService";
import historyService from "../services/historyService";

function TicketsTable(props) {

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [searchColumn, setSearchColumn] = useState("t.name");
  const [searchPattern, setSearchPattern] = useState("");
  const currentUser = JSON.parse(localStorage.getItem("user"));
  const matches = useMediaQuery('(min-width:580px)');

  const handleChangePage = (event, page) => {
    setPage(page);
    props.paginationCallback(page, rowsPerPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setPage(0);
    setRowsPerPage(event.target.value);
    props.paginationCallback(0, event.target.value);
  };

  const handleSelectFilter= (index) => {
    const column = TICKETS_TABLE_COLUMNS[index].db_name;
    setPage(0);
    setSearchColumn(column);
    props.searchCallback(column, searchPattern);
  };

  const handleSelectFilterPattern = event => {
    setPage(0);
    setSearchPattern(event.target.value);
    props.searchCallback(searchColumn, event.target.value);
  };

  const handleSelectAction = (ticket) => (action) => {
    ticketService.getTicket(ticket.id)
      .then((response) => {
        if (response.status === 200) {
          let newTicket = response.data;
          const prevStatus = response.data.status;
          if (currentUser.role === 'ROLE_EMPLOYEE') {
            newTicket = {
              ...response.data,
              statusId: action, 
            }
          } else if (currentUser.role === 'ROLE_MANAGER') {
            newTicket = {
              ...response.data,
              statusId: action, 
              approverId: currentUser.id,
            }
          } else if (currentUser.role === 'ROLE_ENGINEER') {
            newTicket = {
              ...response.data,
              statusId: action, 
              assigneeId: currentUser.id,
            }
          }
          ticketService.putTicket(newTicket.id, newTicket)
            .then((response) => {
              if (response.status === 204) {

                const history = {
                  ticketId: newTicket.id,
                  date: new Date(),
                  userId: currentUser.id,
                  action: "Ticket status is changed",
                  description: "Ticket status is changed from \"" + prevStatus + "\" to \"" + STATUSES[action] + "\"",
                };
                historyService.postHistory(newTicket.id, history);
              }

              props.selectActionCallback();
            });
        }
      });
  };

    const { 
      searchErrorMessage, 
      tickets, 
      sortCallback , 
      orderBy, 
      order, 
      ticketsCount 
    } = props;

    const { url } = props.match;

    return (
      <Paper>
        <TableContainer>
          <div className="search-bar">
            <DropDown 
              style={{width: "120px"}}
              label="Search by:"
              options={TICKETS_TABLE_COLUMNS.slice(0, 5)}
              selectedIndex={1}
              onSelect={handleSelectFilter}
            />
            <div>
            <TextField
              error={searchErrorMessage && true}
              id="filled-full-width"
              style={{ margin: 5, width: "500px" }}
              placeholder="Search for ticket"
              margin="normal"
              onChange={handleSelectFilterPattern}
              InputLabelProps={{
                shrink: true,
              }}
            />
              <div>
                {searchErrorMessage && (<span className="form__input-error"> {searchErrorMessage} </span>)}
              </div>
            </div>
          </div>
          <Table>
            <TableHead>
              <TableRow>
                {TICKETS_TABLE_COLUMNS.map((column) => {
                  if (column.id !== "action") {
                    return (
                      <TableCell
                        align={column.align} 
                        key={column.id}
                        sortDirection={orderBy === column.id ? order : false}
                      >
                        <Tooltip
                          title="Sort"
                          enterDelay={300}
                        >
                          <TableSortLabel
                            active={orderBy === column.db_name}
                            direction={order}
                            onClick={() => sortCallback(column.db_name)}
                          >
                            <b>{column.label}</b>
                          </TableSortLabel>
                        </Tooltip>
                      </TableCell>
                    )
                  } else {
                    return (
                      <TableCell
                        align={column.align} 
                        key={column.id}
                      >
                        <b>{column.label}</b>
                      </TableCell>
                    )
                  }
                })
                }
              </TableRow>
            </TableHead>
            <TableBody>
              {tickets.map((ticket, index) => {
                  return (
                    <TableRow hover role="checkbox" key={index}>
                      {TICKETS_TABLE_COLUMNS.map((column) => {
                        const value = ticket[column.id];
                        if (column.id === "name") {
                          return (
                            <TableCell key={column.id}>
                              <Link to={`${url}/${ticket.id}`}>{value ? value : "......."}</Link>
                            </TableCell>
                          );
                        }
                        if (column.id === "resolutionDate") {
                          return (
                            <TableCell key={column.id}>
                              {value}
                            </TableCell>
                          );
                        }
                        if (column.id === "action") {
                          return (
                            <TableCell align="center" key={column.id}>
                              <SplitButton
                                label="Action"
                                options={ticket.actions}
                                onSelect={handleSelectAction(ticket)}
                               />
                            </TableCell>
                          );
                        } else {
                          return <TableCell key={column.id}>{value}</TableCell>;
                        }
                      })}
                    </TableRow>
                  );
                })}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={matches ? [2, 5, 10, 25, { label: "All", value: ticketsCount}] : []}
          component="div"
          count={ticketsCount}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          ActionsComponent={matches ? TablePaginationActions : undefined}
        />
      </Paper>
    );
  }


TicketsTable.propTypes = {
  searchCallback: PropTypes.func,
  tickets: PropTypes.array,
  sortCallback: PropTypes.func,
  orderBy: PropTypes.string,
  order: PropTypes.string,
  paginationCallback: PropTypes.func,
  ticketsCount: PropTypes.number,
};

const TicketsTableWithRouter = withRouter(TicketsTable);
export default TicketsTableWithRouter;
