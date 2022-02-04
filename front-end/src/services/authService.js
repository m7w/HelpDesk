import apiService from "./apiService.js";

const authService = {
    login : (email, password) => {
        return apiService.post("/api/auth/login", {
            email: email,
            password: password,
        });
    }
}

export default authService;
