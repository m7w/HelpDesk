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
import { Link, withRouter } from "react-router-dom";
import { ALL_TICKETS } from "../constants/mockTickets";
import categoryService from "../services/categoryService";
import ticketService from "../services/ticketService";
import commentService from "../services/commentService";
import attachmentService from "../services/attachmentService";
import Alert from "./Alert";

class TicketCreationPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      nameValue: "",
      resolutionDateValue: new Date(),
      urgencyValue: 0, //Critical
      statusValue: 1,  //New
      categoryValue: 4,//Products & services
      descriptionValue: "",
      commentValue: "",
      attachmentValue: [],
      attachmentNumber: 0,
      attachmentError: "",
      nameError: "",
      CATEGORIES_OPTIONS: [],
      URGENCY_OPTIONS: [],
    };
  }

  componentDidMount() {
    categoryService.getCategories()
      .then((response) => {
        this.setState({CATEGORIES_OPTIONS: response.data});
      });

    ticketService.getUrgencies()
      .then((response) => {
        this.setState({URGENCY_OPTIONS: response.data});
      })

    // set request for getting ticket in draft state
    ticketService.getDraft()
      .then((response) => {
        const ticketData = response.data;

        if (ticketData.id) {
          this.setState({
            nameValue: ticketData.name,
            resolutionDateValue: ticketData.resolutionDate,
            urgencyValue: ticketData.urgency,
            categoryValue: ticketData.category,
            descriptionValue: ticketData.description,
            commentValue: ticketData.comment,
          });
        }
      });
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
        attachmentError: "The selected file type is not allowed. \
        Please select a file of one of the following types: pdf, png, doc, docx, jpg, jpeg.",
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
      }
      this.setState({
        attachmentValue: [...this.state.attachmentValue, attachment], 
        attachmentNumber: this.state.attachmentNumber + 1,
        attachmentError: "",
      });
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

  handleAttachmentDelete = (key) => {
    this.setState({
      attachmentValue: [...this.state.attachmentValue].filter((a) => a.key !== key),
    });
  }

  handleAttachmentDownload = (key) => {
    const attachment = this.state.attachmentValue[key];
    const link = document.createElement('a');
    link.href = URL.createObjectURL(attachment.file);
    link.download = attachment.file.name;
    document.body.appendChild(link);
    link.click();
    link.parentNode.removeChild(link);
  }

  handleCommentChange = (event) => {
    this.setState({ commentValue: event.target.value });
  };

  getFormattedDate = (date) => {
    let year = date.getFullYear();
    let month = (1 + date.getMonth()).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    return day + '/' + month + '/' + year;
  };

  handleSaveDraft = () => {
    // put change of status to draft here
    this.setState({ statusValue: 0,
    }, () => {
      this.handleSubmitTicket();
    });
  };

  handleSubmitTicket = () => {
    // put submit logic here
    const user = JSON.parse(localStorage.getItem("user"));
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
      urgency: urgencyValue,
      status: statusValue,
      ticketOwnerId: user.id,
      category: categoryValue,
      description: descriptionValue,
      comment: commentValue,
    }
    
    ticketService.postTicket(ticket)
      .then((response) => {
        if (response.status === 201) {
          const ticketId = response.data;

          if (commentValue) {
            const comment = {
              userId: user.id,
              text: commentValue,
              date: date,
              ticketId: ticketId, 
            }

            commentService.postComment(ticketId, comment);
          }

          if (attachmentValue) {
            attachmentValue.forEach((attachment) => {
              const formData = new FormData();
              formData.append("file", attachment.file);
              attachmentService.postAttachment(ticketId, formData);
                //.then((response) => {
                  //console.log(response.data);
                //});
            });
          }
        }

        this.props.history.push("/main-page");
      });
  };

  handleValidateInput = (event) => {
    event.target.value = event.target.value.replace(/[^ A-Za-z0-9~."(),:;<>@[\]!#$%&'*+-/=?^_`{|}]/g, "");
  };

  render() {
    const {
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
          <Button component={Link} to="/main-page" variant="contained">
            Ticket List
          </Button>
        </header>
        <div className="ticket-creation-form-container__title">
          <Typography display="block" variant="h3">
            Create new ticket
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
                  value={categoryValue}
                  label="Category"
                  onChange={this.handleCategoryChange}
                  inputProps={{
                    name: "category",
                    id: "category-label",
                  }}
                >
                  {CATEGORIES_OPTIONS.map((item) => {
                    return (
                      <MenuItem value={item.id} key={item.id}>
                        {item.name}
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
                  value={urgencyValue}
                  label="Urgency"
                  onChange={this.handleUrgencyChange}
                  className={"ticket-creation-input_width200"}
                  inputProps={{
                    name: "urgency",
                    id: "urgency-label",
                  }}
                >
                  {Object.entries(URGENCY_OPTIONS).map((entry, index) => {
                    return (
                      <MenuItem value={entry[0]} key={entry[0]}>
                        {entry[1]}
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
                {attachmentValue.map((attachment) => {
                  return (
                    <Chip
                      variant="outlined"
                      size="small"
                      label={attachment.file.name}
                      key={attachment.key}
                      onDelete={() => this.handleAttachmentDelete(attachment.key)}
                      onClick={() => this.handleAttachmentDownload(attachment.key)}
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
                  rows={4}
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
                  rows={4}
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
            <Button variant="contained" onClick={this.handleSaveDraft}>
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
