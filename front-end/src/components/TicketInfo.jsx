import React from "react";
import PropTypes from "prop-types";
import CommentsTable from "./CommentsTable";
import HistoryTable from "./HistoryTable";
import TabPanel from "./TabPanel";
import TicketCreationPageWithRouter from "./TicketCreationPage";
import { Link, Route, Switch } from "react-router-dom";
import { withRouter } from "react-router";
import {
  Chip,
  Button,
  ButtonGroup,
  Paper,
  Tab,
  Tabs,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
  TextField,
} from "@material-ui/core";

import attachmentService from "../services/attachmentService";
import commentService from "../services/commentService";
import historyService from "../services/historyService";
import ticketService from "../services/ticketService";
import feedbackService from "../services/feedbackService";

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}

class TicketInfo extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      commentValue: "",
      currentUser: JSON.parse(localStorage.getItem("user")),
      feedbackBtnTitle: "",
      tabValue: 0,
      ticketAttachments: [],
      ticketComments: [],
      ticketHistory: [],
      ticketData: {
        id: 0,
        name: "",
        date: "",
        category: "",
        status: "",
        statusId: 0,
        urgency: "",
        resolutionDate: "",
        owner: "",
        ownerId: 0,
        approver: "",
        assignee: "",
        assigneeId: 0,
        description: "",
      },
    };
  }

  componentDidMount() {
    // get required ticket by id

    const { ticketId } = this.props.match.params;
    ticketService.getTicket(ticketId).then((response) => {
      this.setState({
        ticketData: response.data,
      });

      this.handleFeedbackButton();

      attachmentService.getAttachmentsInfo(ticketId).then((response) => {
        this.setState({
          ticketAttachments: response.data,
        });
      });

      historyService.getHistory(ticketId).then((response) => {
        this.setState({
          ticketHistory: response.data,
        });
      });

      commentService.getComments(ticketId).then((response) => {
        this.setState({
          ticketComments: response.data,
        });
      });
    });
  }

  handleAttachmentDownload = (id, name) => {
    attachmentService.getAttachment(this.state.ticketData.id, id).then((response) => {
      const link = document.createElement("a");
      const blob = new Blob([response.data], { type: "application/octet-stream" });
      link.href = URL.createObjectURL(blob);
      link.download = name;
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
    });
  };

  handleTabChange = (event, value) => {
    this.setState({
      tabValue: value,
    });
  };

  handleValidateInput = (event) => {
    event.target.value = event.target.value.replace(
      /[^ A-Za-z0-9~."(),:;<>@[\]!#$%&'*+-/=?^_`{|}]/g,
      ""
    );
  };

  handleEnterComment = (event) => {
    this.setState({
      commentValue: event.target.value,
    });
  };

  addComment = () => {
    // put request for comment creation here

    const { commentValue } = this.state;
    const { ticketId } = this.props.match.params;

    if (commentValue) {
      const comment = {
        userId: this.state.currentUser.id,
        text: commentValue,
        date: new Date(),
        ticketId: ticketId,
      };

      commentService.postComment(ticketId, comment).then((response) => {
        if (response.status === 201) {
          commentService.getComments(ticketId).then((response) => {
            this.setState({
              ticketComments: response.data,
              commentValue: "",
            });
          });
        }
      });
    }
  };

  handleSubmitTicket = () => {
    // set ticket status to 'submitted'
    this.handleAction(1);
  };

  handleEditTicket = () => {};

  handleCancelTicket = () => {
    // set ticket status to 'canceled' status
    this.handleAction(6);
  };

  handleAction = (newStatus) => {
    let approverId = null;
    if (this.state.currentUser.role === "ROLE_MANAGER") {
      approverId = this.state.currentUser.id;
    }
    this.setState(
      (prevState) => ({
        ticketData: {
          ...prevState.ticketData,
          statusId: newStatus,
          approverId: approverId,
        },
      }),
      this.handleUpdateStatus
    );
  };

  handleUpdateStatus = () => {
    const { ticketData } = this.state;
    ticketService.putTicket(ticketData.id, ticketData).then((response) => {
      if (response.status === 204) {
        let status = "";
        if (ticketData.statusId === 1) {
          status = '"New"';
        } else if (ticketData.statusId === 6) {
          status = '"Canceled"';
        }

        const history = {
          ticketId: ticketData.id,
          date: new Date(),
          userId: this.state.currentUser.id,
          action: "Ticket status is changed",
          description: 'Ticket status is changed from "Draft" to ' + status,
        };
        historyService.postHistory(ticketData.id, history);

        this.props.history.push("/main-page");
        this.props.onAction();
      }
    });
  };

  handleFeedbackButton = () => {
    const { id, ownerId, assigneeId, status } = this.state.ticketData;
    if (this.state.currentUser.id === ownerId && status === "Done") {
      feedbackService
        .getFeedback(id)
        .then((response) => {
          if (response.status === 200) {
            this.setState({ feedbackBtnTitle: "View Feedback" });
          }
        })
        .catch((error) => {
          if (error.response.status === 404) {
            this.setState({ feedbackBtnTitle: "Leave Feedback" });
          }
        });
    }
    if (this.state.currentUser.id === assigneeId && status === "Done") {
      feedbackService.getFeedback(id).then((response) => {
        if (response.status === 200) {
          this.setState({ feedbackBtnTitle: "View Feedback" });
        }
      });
    }
  };

  render() {
    const {
      approver,
      id,
      name,
      date,
      category,
      status,
      urgency,
      resolutionDate,
      owner,
      assignee,
      description,
    } = this.state.ticketData;

    const {
      commentValue,
      tabValue,
      ticketAttachments,
      ticketComments,
      ticketHistory,
      feedbackBtnTitle,
    } = this.state;

    const { url } = this.props.match;

    const { handleCancelTicket, handleEditTicket, handleSubmitTicket } = this;

    return (
      <Switch>
        <Route exact path={url}>
          <div className="ticket-data-container">
            <div className={"ticket-data-container__head-buttons-raw"}>
              <div className={"ticket-data-container__back-button back-button"}>
                <Button component={Link} to="/main-page" variant="contained">
                  Ticket list
                </Button>
              </div>
              <div className={"ticket-data-container__feedback-button"}>
                {feedbackBtnTitle && (
                  <Button
                    variant="contained"
                    component={Link}
                    to={{
                      pathname: "/feedback",
                      id: id,
                      name: name,
                      readOnly: feedbackBtnTitle === "View Feedback",
                    }}
                  >
                    {feedbackBtnTitle}
                  </Button>
                )}
              </div>
            </div>
            <div className="ticket-data-container__title">
              <Typography variant="h4">{`Ticket(${id}) - ${name}`}</Typography>
            </div>
            <div className="ticket-data-container__info">
              <TableContainer className="ticket-table" component={Paper}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Created on:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {date}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Category:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {category}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Status:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {status}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Urgency:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {urgency}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Desired Resolution Date:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {resolutionDate}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Owner:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {owner}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Approver:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {approver || "Not assigned"}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Assignee:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {assignee || "Not assigned"}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Attachments:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          <div className="info-section-attachment">
                            {ticketAttachments.length > 0
                              ? ticketAttachments.map((attachment) => {
                                  return (
                                    <Chip
                                      variant="outlined"
                                      size="small"
                                      label={attachment.name}
                                      key={attachment.id}
                                      onClick={() =>
                                        this.handleAttachmentDownload(
                                          attachment.id,
                                          attachment.name
                                        )
                                      }
                                    />
                                  );
                                })
                              : "Not assigned"}
                          </div>
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Description:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {description || "Not assigned"}
                        </Typography>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </div>
            {status.toLowerCase() === "draft" && (
              <div className="ticket-data-container__button-section">
                <ButtonGroup variant="contained" color="primary">
                  <Button onClick={handleSubmitTicket}>Submit</Button>

                  <Button component={Link} to={`/create-ticket/${id}`} onClick={handleEditTicket}>
                    Edit
                  </Button>
                  <Button onClick={handleCancelTicket}>Cancel</Button>
                </ButtonGroup>
              </div>
            )}
            <div className="ticket-data-container__comments-section comments-section">
              <div className="">
                <Tabs
                  variant="fullWidth"
                  onChange={this.handleTabChange}
                  value={tabValue}
                  indicatorColor="primary"
                  textColor="primary"
                >
                  <Tab label="History" {...a11yProps(0)} />
                  <Tab label="Comments" {...a11yProps(1)} />
                </Tabs>
                <TabPanel value={tabValue} index={0}>
                  <HistoryTable history={ticketHistory} />
                </TabPanel>
                <TabPanel value={tabValue} index={1}>
                  <CommentsTable comments={ticketComments} />
                </TabPanel>
              </div>
            </div>
            {tabValue && (
              <div className="ticket-data-container__enter-comment-section enter-comment-section">
                <TextField
                  label="Enter a comment"
                  multiline
                  minRows={4}
                  value={commentValue}
                  variant="filled"
                  className="comment-text-field"
                  onChange={this.handleEnterComment}
                  inputProps={{
                    maxLength: 500,
                  }}
                  onInput={this.handleValidateInput}
                />
                <div className="enter-comment-section__add-comment-button">
                  <Button variant="contained" color="primary" onClick={this.addComment}>
                    Add Comment
                  </Button>
                </div>
              </div>
            )}
          </div>
        </Route>
        <Route path="/create-ticket/:ticketId">
          <TicketCreationPageWithRouter />
        </Route>
      </Switch>
    );
  }
}

TicketInfo.propTypes = {
  match: PropTypes.object,
};

const TicketInfoWithRouter = withRouter(TicketInfo);
export default TicketInfoWithRouter;
