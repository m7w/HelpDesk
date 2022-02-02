import axios from "axios"

const authService = axios.create({
    method: "post",
    baseURL: "http://localhost:8080/helpdesk/api", 
});

export default authService;
