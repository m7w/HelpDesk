import apiService from "./apiService";

const getCurrentUser = () => {
    const token = localStorage.getItem("token");
    const encPayload = token.split(".")[1];
    const strPayload = Buffer.from(encPayload, "base64").toString();
    const id = JSON.parse(strPayload).id;

    return apiService.get(`/api/users/${id}`);
};

const userService = {
    getCurrentUser,
}

export default userService;
