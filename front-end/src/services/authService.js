import apiService from "./apiService.js";

const login = (email, password) => {
    return apiService.post("/api/auth/login", {
        email: email,
        password: password,
    });
};

const authService = {
    login,
};

export default authService;
