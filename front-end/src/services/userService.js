import apiService from "./apiService";

const getCurrentUser = () => {
  console.log("int getCurrentUser");
  const token = localStorage.getItem("token");
  console.log("token: " + token);
  const encPayload = token.split(".")[1];
  console.log("encPayload: " + encPayload);
  //const strPayload = Buffer.from(encPayload, "base64").toString();
  const strPayload = atob(encPayload);
  console.log("strPayload: " + strPayload);
  const id = JSON.parse(strPayload).id;

  return apiService.get(`/api/users/${id}`);
};

const userService = {
  getCurrentUser,
}

export default userService;
