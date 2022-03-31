import React from "react";
import PropTypes from "prop-types";
import {
  ButtonGroup,
  Button,
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
} from "@material-ui/core";
import { Link } from "react-router-dom";
import { withRouter } from "react-router";
import { TICKETS_TABLE_COLUMNS } from "../constants/tablesColumns";
import DropDown from "./DropDown";
import TablePaginationActions from "./TablePaginationActions";

class TicketsTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      page: 0,
      rowsPerPage: 5,
      searchColumn: "t.name",
      searchPattern: "",
    };
  }

  handleChangePage = (event, page) => {
    this.setState({ page: page });
    this.props.paginationCallback(page, this.state.rowsPerPage);
    console.log("handleChangePage");
  };

  handleChangeRowsPerPage = (event) => {
    this.setState({ page: 0, rowsPerPage: event.target.value });
    this.props.paginationCallback(0, event.target.value);
    console.log("handleChangeRowsPerPage");
  };

  handleSelectFilter= (index) => {
    const column = TICKETS_TABLE_COLUMNS[index].db_name;
    this.setState({ page: 0, searchColumn: column });
    this.props.searchCallback(column, this.state.searchPattern);
  };

  handleSelectFilterPattern = event => {
    this.setState({ page: 0, searchPattern: event.target.value });
    this.props.searchCallback(this.state.searchColumn, event.target.value);
  };

  handleCancelSubmit = () => {
    console.log("Cancel submit");
  };

  handleSubmitTicket = () => {
    console.log("Submit ticket");
  };

  render() {
    const {
      handleChangePage,
      handleChangeRowsPerPage,
      handleCancelSubmit,
      handleSubmitTicket,
      handleSelectFilter,
      handleSelectFilterPattern,
    } = this;

    const { 
      searchErrorMessage, 
      tickets, 
      sortCallback , 
      orderBy, 
      order, 
      ticketsCount 
    } = this.props;

    const { page, rowsPerPage } = this.state;
    const { url } = this.props.match;

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
              {tickets.map((row, index) => {
                  return (
                    <TableRow hover role="checkbox" key={index}>
                      {TICKETS_TABLE_COLUMNS.map((column) => {
                        const value = row[column.id];
                        if (column.id === "name") {
                          return (
                            <TableCell key={column.id}>
                              <Link to={`${url}/${row.id}`}>{value ? value : "......."}</Link>
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
                          return row.status !== "draft" ? (
                            <TableCell align="center" key={column.id}>
                              <DropDown 
                                style={{width: "120px"}}
                                options={[{id: 0, label: "Submit"}, {id: 1, label: "Reject"}]}
                                selectedIndex={0}
                              />
                            </TableCell>
                          ) : (
                            <TableCell key={column.id}></TableCell>
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
          rowsPerPageOptions={[2, 5, 10, 25, { label: "All", value: ticketsCount}]}
          component="div"
          count={ticketsCount}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          ActionsComponent={TablePaginationActions}
        />
      </Paper>
    );
  }
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
