import React from "react";
import { Redirect, Route } from "react-router-dom";

function ProtectedRoute({ component: Component,  condition, ...restOfProps }) {

  return (
    <Route
      {...restOfProps}
      render={(props) =>
        condition ? <Component {...props} /> : <Redirect to="/main-page" />
      }
    />
  );
}

export default ProtectedRoute;
