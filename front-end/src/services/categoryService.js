import apiService from "./apiService";

const getCategories = () => {

    return apiService.get("/api/categories");
};

const categoryService = {
    getCategories,
}

export default categoryService;

