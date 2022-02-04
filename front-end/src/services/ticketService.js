import apiService from "./apiService";

const ticketService = {
    getTickets : () => {
        return apiService.get("/api/tickets/employee");
    }
}

export default ticketService;
