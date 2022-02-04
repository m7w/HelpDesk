import axios from "axios"

const apiService = axios.create();

const apiConfigure = (contextPath) => {
    apiService.defaults.baseURL = contextPath;
}

apiService.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
    } 
    return config;
}, (error) => {
    return Promise.reject(error);
});

apiService.interceptors.response.use((response) => {
    return response;
}, (error) => {
    return Promise.reject(error);
});

export { apiConfigure };
export default apiService;
