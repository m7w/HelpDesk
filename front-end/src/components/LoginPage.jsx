import React, { useState } from "react";
import { Grid, Button, TextField, Typography } from "@material-ui/core";
import authService from "../services/authService";
import userService from "../services/userService";
import Alert from "./Alert";

function LoginPage(props) {

  const [nameValue, setNameValue] = useState("");
  const [passwordValue, setPasswordValue] = useState("");
  const [nameError, setNameError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [authError, setAuthError] = useState("");

  const handleNameChange = (event) => {
    setNameValue(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPasswordValue(event.target.value);
  };

  const handleClickAuth = () => {
    // put authorization logic here
  
    authService.login(nameValue, passwordValue)
      .then((response) => {
        if (response.status === 200) {
          const token = response.headers.authorization;
          localStorage.setItem("token", token);
          userService.getCurrentUser()
            .then((response) => {
              localStorage.setItem("user", JSON.stringify(response.data));
              props.userCallback(response.data);
              props.authCallback(true);
            })
        }
      })
      .catch((error) => {

        switch (error.response.status) {
          case 400:
            setNameError(error.response.data.email);
            setPasswordError(error.response.data.password);
            break;
          case 401:
            setNameError("");
            setPasswordError(""); 
            setAuthError("Please make sure you are using a valid email and password");
            break;
          default:
            break;
        }
      });
  }
  
    return (
      <div className="container">
        <div className="container__title-wrapper">
          <Typography component="h2" variant="h5">
            Login to the Help Desk
          </Typography>
        </div>
        {authError &&(<Alert onClose={setAuthError} message={authError}/>)}
        <div className="container__from-wrapper">
          <form>
            <div className="container__inputs-wrapper">
              <div className="form__input-wrapper">
                <div className="label__input-wrapper">
                  <label>User Name:</label>
                </div>
                <TextField
                  onChange={handleNameChange}
                  label="User name"
                  variant="outlined"
                  placeholder="User name"
                />
              </div>
              {nameError && (<span className="form__input-error"> {nameError} </span>)}
              <div className="form__input-wrapper">
                <div className="label__input-wrapper">
                  <label>Password:</label>
                </div>
                <TextField
                  onChange={handlePasswordChange}
                  label="Password"
                  variant="outlined"
                  type="password"
                  placeholder="Password"
                />
              </div>
              {passwordError && (<span className="form__input-error"> {passwordError} </span>)} </div>
          </form>
        </div>
        <div className="container__button-wrapper">
        <Grid container justifyContent="flex-end">
          <Button
            size="medium"
            variant="contained"
            color="primary"
            onClick={handleClickAuth}
          >
            Enter
          </Button>
        </Grid>
        </div>
      </div>
    );
};

export default LoginPage;
