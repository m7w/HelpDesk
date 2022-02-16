import apiService from "./apiService";

const getTickets = (page, size, orderBy, order) => {
    return apiService.get(`/api/tickets?page=${page}&size=${size}&sort=${orderBy}.${order}`);
};

const ticketService = {
    getTickets,
}

export default ticketService;
