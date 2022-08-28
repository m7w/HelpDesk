import React from "react";
import {
  Button,
  Chip,
  InputLabel,
  FormControl,
  MenuItem,
  Select,
  TextField,
  Typography,
  Tooltip,
} from "@material-ui/core";
import DateFnsUtils from "@date-io/date-fns";
import {
  MuiPickersUtilsProvider, 
  KeyboardDatePicker 
} from "@material-ui/pickers";
import { withRouter } from "react-router-dom";
import categoryService from "../services/categoryService";
import ticketService from "../services/ticketService";
import commentService from "../services/commentService";
import attachmentService from "../services/attachmentService";
import historyService from "../services/historyService";
import Alert from "./Alert";

class TicketCreationPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      ticketId: 0,
      nameValue: "",
      resolutionDateValue: new Date(),
      urgencyValue: "", //Critical
      statusValue: 1,  //New
      categoryValue: "",//Products & services
      descriptionValue: "",
      commentValue: "",
      attachmentValue: [],
      attachmentNumber: 0,
      attachmentError: "",
      nameError: "",
      CATEGORIES_OPTIONS: [],
      URGENCY_OPTIONS: [],
      currentUser: JSON.parse(localStorage.getItem("user")),
    };
  }

  componentDidMount() {
    // set request for getting ticket in draft state
    const ticketFromUrl = this.props.location.pathname.split("/");
    const ticketId = ticketFromUrl[ticketFromUrl.length - 1];

    categoryService.getCategories()
      .then((response) => {
        this.setState({CATEGORIES_OPTIONS: response.data});
        this.setState({categoryValue: 4});
      });

    ticketService.getUrgencies()
      .then((response) => {
        this.setState({URGENCY_OPTIONS: response.data});
        this.setState({urgencyValue: 0});
      })

    if (/^\d+$/.test(ticketId)) {
      ticketService.getTicket(ticketId)
        .then((response) => {
          const ticketData = response.data;
          if (ticketData) {
            this.setState({
              ticketId: ticketId,
              ticketStatusId: ticketData.statusId,
              nameValue: ticketData.name,
              resolutionDateValue: new Date(Date.parse(ticketData.resolutionDate.split("/").reverse().join("-"))),
              urgencyValue: ticketData.urgencyId,
              categoryValue: ticketData.categoryId,
              descriptionValue: ticketData.description,
              commentValue: ticketData.comment,
            });
          }

          attachmentService.getAttachmentsInfo(ticketId)
            .then((response) => {
              if (response.data) {
                this.setState({
                  attachmentValue: response.data.map((a) => {return {key: a.id, file: a.name, uploaded: true}}),
                  attachmentNumber: Math.max.apply(null, response.data.map((a) => {return a.id})) + 1,
                });
              }
            });
        });
    }
  };

  handleCategoryChange = (event) => {
    this.setState({ categoryValue: event.target.value });
    console.log(event.target.value);
  };

  handleNameChange = (event) => {
    this.setState({
      nameValue: event.target.value,
      nameError: "",
    });
  };

  handleDescriptionChange = (event) => {
    this.setState({ descriptionValue: event.target.value });
  };

  handleUrgencyChange = (event) => {
    this.setState({ urgencyValue: event.target.value });
    console.log("urgency: " + event.target.value);
  };

  handleResolutionDate = (date) => {
    this.setState({ resolutionDateValue: date });
  };

  handleAttachmentAlert = (message) => {
    this.setState({ attachmentError: message });
  }

  handleAttachmentChange = (event) => {
    const file = event.target.files[0];
    if (!this.checkFileType(file.type)) {
      this.setState({
        attachmentError: "The type of the attached file is not allowed. \
        Please select a file of one of the following types: pdf, doc, docx, png, jpg, jpeg.",
      });
    } else if (!this.checkFileSize(file.size)) {
      this.setState({
        attachmentError: "The size of the attached file should not be greater than 5 Mb. \
        Please select another file.",
      });
    } else {
      const attachment = {
        key: this.state.attachmentNumber,
        file: file,
        uploaded: false,
      }
      this.setState({
        attachmentValue: [...this.state.attachmentValue, attachment], 
        attachmentNumber: this.state.attachmentNumber + 1,
        attachmentError: "",
      }, () => console.log(this.state.attachmentValue));
    }
  };

  checkFileType = (type) => {
    const types = ["application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
      "application/msword", "application/pdf", "image/png", "image/jpeg", "image/jpg"];
    if (types.includes(type)) {
      return true;
    }
    return false;
  }

  checkFileSize = (size) => {
    if (size <= 5 * 1024 * 1024 ) {
      return true;
    }
    return false;
  }

  handleAttachmentDelete = (index) => {
    const attachment = this.state.attachmentValue[index];
    if (attachment.uploaded) {
      attachmentService.deleteAttachment(this.state.ticketId, attachment.key)
        .then((response) => {
          if (response.status === 204) {
            const history = {
              ticketId: this.state.ticketId,
              date: new Date(),
              userId: this.state.currentUser.id,
              action: "File is removed",
              description: "File is removed: \"" + attachment.file + "\"",
            };
            historyService.postHistory(this.state.ticketId, history);
          }
        });
    }
    this.setState({
      attachmentValue: [...this.state.attachmentValue].filter((a) => a.key !== attachment.key),
    });
  }

  handleAttachmentDownload = (index) => {
    const attachment = this.state.attachmentValue[index];
    if (attachment.uploaded) {
      attachmentService.getAttachment(this.state.ticketId, attachment.key)
        .then((response) => {
          const blob = new Blob([response.data], {type: "application/octet-stream"});
          this.getFile(attachment.file, blob);
        });
    } else {
      this.getFile(attachment.file.name, attachment.file);
    }
  }

  getFile = (name, blob) => {
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = name;
    document.body.appendChild(link);
    link.click();
    link.parentNode.removeChild(link);
  }

  handleCommentChange = (event) => {
    this.setState({ commentValue: event.target.value });
  };

  getFormattedDate = (date) => {
    console.log("date: " + date);
    let year = date.getFullYear();
    let month = (1 + date.getMonth()).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    return day + '/' + month + '/' + year;
  };

  handleSaveDraft = () => {
    // put change of status to draft here
    this.setState({ statusValue: 0 }, this.handleSubmitTicket);
  };

  handleSubmitTicket = () => {
    // put submit logic here
    const user = this.state.currentUser;
    var date = new Date();

    const {
      nameValue,
      resolutionDateValue,
      urgencyValue,
      statusValue,
      categoryValue,
      attachmentValue,
      descriptionValue,
      commentValue,
    } = this.state

    if (statusValue && !nameValue) {
      this.setState({ nameError: "Please fill out the required field."});
      return;
    }

    const ticket = {
      name: nameValue,
      date: this.getFormattedDate(date),
      resolutionDate: this.getFormattedDate(resolutionDateValue),
      urgencyId: urgencyValue,
      statusId: statusValue,
      ownerId: user.id,
      categoryId: categoryValue,
      description: descriptionValue,
    }
    
    if (this.state.ticketId) {
      const ticketId = this.state.ticketId;
      ticketService.putTicket(ticketId, ticket)
        .then((response) => {
          if (response.status === 204) {
            saveComment(ticketId);
            saveAttachments(ticketId);
            saveHistory(ticketId, "Ticket is edited", "Ticket is edited");
            if (ticket.statusId !== this.state.ticketStatusId) {
              saveHistory(ticketId, "Ticket status is changed", 
                "Ticket status is changed from \"Draft\" to \"New\"");
            }
            this.props.history.goBack();
          }
        });
    } else {
      ticketService.postTicket(ticket)
        .then((response) => {
          if (response.status === 201) {
            const ticketId = response.data;
            saveComment(ticketId);
            saveAttachments(ticketId);
            saveHistory(ticketId, "Ticket is created", "Ticket is created");
            this.props.history.goBack();
          }

        });
    }

    const saveComment = (ticketId) => {
      if (commentValue) {
        const comment = {
          userId: user.id,
          text: commentValue,
          date: date,
          ticketId: ticketId, 
        }

        commentService.postComment(ticketId, comment);
      }
    }

    const saveAttachments = (ticketId) => {
      if (attachmentValue) {
        attachmentValue.forEach((attachment) => {
          if (!attachment.uploaded) {
            const formData = new FormData();
            formData.append("file", attachment.file);
            attachmentService.postAttachment(ticketId, formData)
              .then((response) => {
                const fileName = attachment.file.name;
                console.log("Uploaded: " + fileName);

                const history = {
                  ticketId: ticketId,
                  date: new Date(),
                  userId: user.id,
                  action: "File is attached",
                  description: "File is attached: \"" + fileName + "\"",
                };
                historyService.postHistory(ticketId, history);
              })
              .catch((error) => {
                let message;
                if (error.response.data) {
                  message = error.response.data.message;
                } else {
                  message = "Something went wrong.";
                }
                alert(`Uploading ${attachment.file.name}: ${message}\nStatus: ${error.response.status}`);
              });
          }
        });
      }
    }

    const saveHistory = (ticketId, action, description) => {
          const history = {
            ticketId: ticketId,
            date: new Date(),
            userId: user.id,
            action: action,
            description: description,
          };
          historyService.postHistory(ticketId, history);
    }

  };

  handleValidateInput = (event) => {
    event.target.value = event.target.value.replace(/[^ A-Za-z0-9~."(),:;<>@[\]!#$%&'*+-/=?^_`{|}]/g, "");
  };

  render() {
    const {
      ticketId,
      nameValue,
      categoryValue,
      commentValue,
      descriptionValue,
      resolutionDateValue,
      urgencyValue,
      attachmentValue,
      attachmentError,
      nameError,
      CATEGORIES_OPTIONS,
      URGENCY_OPTIONS,
    } = this.state;

    const helperText = "It is allowed to enter lowercase English alpha characters, \
      digits and special characters: ~.\"(),:;<>@[]!#$%&'*+-/=?^_`{|}"

    return (
      <div className="ticket-creation-form-container">
        <header className="ticket-creation-form-container__navigation-container">
          <Button variant="contained" onClick={this.props.history.goBack}>
            {ticketId ? "Ticket Overview" : "Ticket List"}
          </Button>
        </header>
        <div className="ticket-creation-form-container__title">
          <Typography display="block" variant="h3">
            {ticketId ? "Edit Ticket" : "Create new Ticket"}
          </Typography>
        </div>
        <div className="ticket-creation-form-container__form">
          <div className="inputs-section">
            <div className="ticket-creation-form-container__inputs-section inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl>
                <Tooltip
                  title={<h2>{helperText}</h2>}>
                  <TextField
                    required
                    label="Name"
                    variant="outlined"
                    onChange={this.handleNameChange}
                    id="name-label"
                    value={nameValue}
                    inputProps={{
                      maxLength: 100,
                    }}
                    onInput = {this.handleValidateInput}
                  />
                </Tooltip>
              </FormControl>
              {nameError && (<span className="form__input-error"> {nameError} </span>)}
            </div>
            <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl variant="outlined" required>
                <InputLabel shrink htmlFor="category-label">
                  Category
                </InputLabel>
                <Select
                  defaultValue=""
                  value={categoryValue}
                  label="Category"
                  onChange={this.handleCategoryChange}
                  inputProps={{
                    name: "category",
                    id: "category-label",
                  }}
                >
                  {CATEGORIES_OPTIONS.map((category) => {
                    return (
                      <MenuItem value={category.id} key={category.id}>
                        {category.name}
                      </MenuItem>
                    );
                  })}
                </Select>
              </FormControl>
            </div>
            <div className="inputs-section__ticket-creation-input ticket-creation-input">
              <FormControl variant="outlined" required>
                <InputLabel shrink htmlFor="urgency-label">
                  Urgency
                </InputLabel>
                <Select
                  defaultValue=""
                  value={urgencyValue}
                  label="Urgency"
                  onChange={this.handleUrgencyChange}
                  className={"ticket-creation-input_width200"}
                  inputProps={{
                    name: "urgency",
                    id: "urgency-label",
                  }}
                >
                  {URGENCY_OPTIONS.map((urgency) => {
                    return (
                      <MenuItem value={urgency.key} key={urgency.key}>
                        {urgency.label}
                      </MenuItem>
                    );
                  })}
                </Select>
              </FormControl>
            </div>
          </div>
          {attachmentError &&(<Alert onClose={this.handleAttachmentAlert} message={attachmentError}/>)}
          <div className="inputs-section-attachment">
            <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl>
                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                  <KeyboardDatePicker
                    disableToolbar
                    variant="inline"
                    format="dd/MM/yyyy"
                    autoOk={true}
                    disablePast={true}
                    minDate={new Date()}
                    margin="normal"
                    id="date-picker-inline"
                    label="Desired resolution date"
                    value={resolutionDateValue}
                    onChange={this.handleResolutionDate}
                    KeyboardButtonProps={{
                      'aria-label': 'change date',
                    }}
                  />
                </MuiPickersUtilsProvider>
              </FormControl>
            </div>
            <div className="ticket-creation-input">
              <FormControl>
                <Typography variant="caption">Add attachment</Typography>
                <TextField
                  type="file"
                  variant="outlined"
                  onChange={this.handleAttachmentChange}
                />
                {attachmentValue.map((attachment, index) => {
                  return (
                    <Chip
                      variant="outlined"
                      color={attachment.uploaded ? "default" : "primary"}
                      size="small"
                      label={attachment.uploaded ? attachment.file : attachment.file.name}
                      key={attachment.key}
                      onDelete={() => this.handleAttachmentDelete(index)}
                      onClick={() => this.handleAttachmentDownload(index)}
                    />
                  )
                }
                )}
              </FormControl>
            </div>
          </div>

          <div className="inputs-section">
            <FormControl>
              <Tooltip
                title={<h2>{helperText}</h2>}>
                <TextField
                  label="Description"
                  multiline
                  minRows={4}
                  variant="outlined"
                  value={descriptionValue}
                  className="creation-text-field creation-text-field_width680"
                  onChange={this.handleDescriptionChange}
                  inputProps={{
                    maxLength: 500,
                  }}
                  onInput = {this.handleValidateInput}
                />
              </Tooltip>
            </FormControl>
          </div>
          <div className="inputs-section">
            <FormControl>
              <Tooltip
                title={<h2>{helperText}</h2>}>
                <TextField
                  label="Comment"
                  multiline
                  minRows={4}
                  variant="outlined"
                  value={commentValue}
                  className="creation-text-field creation-text-field_width680"
                  onChange={this.handleCommentChange}
                  inputProps={{
                    maxLength: 500,
                  }}
                  onInput = {this.handleValidateInput}
                />
              </Tooltip>
            </FormControl>
          </div>
          <section className="submit-button-section">
            <Button 
              variant="contained" 
              onClick={this.handleSaveDraft}
            >
              Save as Draft
            </Button>
            <Button
              variant="contained"
              onClick={this.handleSubmitTicket}
              color="primary"
            >
              Submit
            </Button>
          </section>
        </div>
      </div>
    );
  }
}

const TicketCreationPageWithRouter = withRouter(TicketCreationPage);
export default TicketCreationPageWithRouter;
