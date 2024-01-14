import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { withRouter } from "react-router";
import { Button, Typography, TextField, makeStyles, Box } from "@material-ui/core";
import Rating from "@material-ui/lab/Rating";
import ticketService from "../services/ticketService";
import feedbackService from "../services/feedbackService";

function FeedbackPage(props) {
  const [message, setMessage] = useState("Please, rate your satisfaction with the solution:");
  const [rateValue, setRateValue] = useState(0);
  const [commentValue, setCommentValue] = useState("");
  const [hover, setHover] = useState(-1);
  const [rateError, setRateError] = useState("");

  const labels = {
    1: "Avoid",
    2: "Bad",
    3: "Acceptable",
    4: "Good",
    5: "Exceptional",
  };

  const useStyles = makeStyles({
    root: {
      width: 200,
      display: "flex",
      alignItems: "center",
    },
  });

  const classes = useStyles();

  const handleValidateInput = (event) => {
    event.target.value = event.target.value.replace(
      /[^ A-Za-z0-9~."(),:;<>@[\]!#$%&'*+-/=?^_`{|}]/g,
      ""
    );
  };

  const handleCommentChange = (event) => {
    setCommentValue(event.target.value);
  };

  useEffect(() => {
    if (readOnly) {
      feedbackService.getFeedback(id).then((response) => {
        if (response.status === 200) {
          setMessage("Feedback");
          setRateValue(response.data.rate);
          setCommentValue(response.data.text);
        }
      });
    }
  }, []);

  const handleSubmitFeedback = () => {
    if (rateValue === 0) {
      setRateError("This input is required for submission.");
      return;
    }
    const user = JSON.parse(localStorage.getItem("user"));
    const feedback = {
      userId: user.id,
      ticketId: id,
      rate: rateValue,
      date: new Date(),
      text: commentValue,
    };
    feedbackService.postFeedback(id, feedback).then((response) => {
      props.history.goBack();
    });
  };

  const { id, name, readOnly } = props.location;

  return (
    <div className="ticket-data-container">
      <div className={"ticket-data-container__head-buttons-raw"}>
        <div className={"ticket-data-container__back-button back-button"}>
          <Button onClick={props.history.goBack} variant="contained">
            Back
          </Button>
        </div>
        <div className="feedback-main-container">
          <Typography variant="h4">{`Ticket(${id}) - ${name}`}</Typography>
          <div className="feedback-element-container">
            <Typography variant="h5">{message}</Typography>
          </div>
          <div className={classes.root}>
            <Rating
              name="feedback-raging"
              size="large"
              readOnly={readOnly}
              value={rateValue}
              onChange={(event, newValue) => {
                setRateValue(newValue);
              }}
              onChangeActive={(event, newHover) => {
                setHover(newHover);
              }}
              sx={{
                minWidth: 400,
              }}
            />
            {rateValue !== null && <Box ml={2}>{labels[hover !== -1 ? hover : rateValue]}</Box>}
          </div>
          {rateError && <span className="form__input-error"> {rateError} </span>}
          <div className="feedback-element-container">
            <TextField
              label="Comment"
              multiline
              minRows={7}
              variant="outlined"
              value={commentValue}
              className="creation-text-field creation-text-field_width680"
              onChange={handleCommentChange}
              inputProps={{
                readOnly: readOnly,
              }}
              onInput={handleValidateInput}
            />
          </div>
          {!readOnly && (
            <Button onClick={handleSubmitFeedback} variant="contained">
              Submit
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}

FeedbackPage.propTypes = {
  match: PropTypes.object,
};

const FeedbackPageWithRouter = withRouter(FeedbackPage);
export default FeedbackPageWithRouter;
