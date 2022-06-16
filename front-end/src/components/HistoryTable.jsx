import React, { useState } from "react";
import PropTypes from "prop-types";
import {
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@material-ui/core";
import { HISTORY_TABLE_COLUMNS } from "../constants/tablesColumns";

function HistoryTable(props) {
  const { history } = props;
  
  const [tableBtnText, setTableBtnText] = useState("Show All");
  const [numRecords, setNumRecords] = useState(5);

  const handleTableBtnClick = () => {
    if (tableBtnText === "Show All") {
      setTableBtnText("Hide");
      setNumRecords(history.length);
    } else {
      setTableBtnText("Show All");
      setNumRecords(5);
    }
  };

  return (
    <div>
      <Button 
        onClick={handleTableBtnClick}
        variant="outlined">
        {tableBtnText}
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              {HISTORY_TABLE_COLUMNS.map((item) => {
                return (
                  <TableCell key={item.id} align="center">
                    <Typography variant="h6">{item.label}</Typography>
                  </TableCell>
                );
              })}
            </TableRow>
          </TableHead>
          <TableBody>
            {history.slice(0, numRecords).map((item, index) => {
              return (
                <TableRow key={index}>
                  <TableCell align="center">{new Date(item.date + 'Z').toLocaleString()}</TableCell>
                  <TableCell align="center">{item.user}</TableCell>
                  <TableCell align="center">{item.action}</TableCell>
                  <TableCell align="left">{item.description}</TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

HistoryTable.propTypes = {
  name: PropTypes.array,
};

export default HistoryTable;
