import apiService from "./apiService";

const getHistory = (ticketId) => {

  return apiService.get(`/api/tickets/${ticketId}/history`);
};

const postHistory = (ticketId, history) => {

  return apiService.post(`/api/tickets/${ticketId}/history`, history);
};

const historyService = {
  getHistory,
  postHistory,
}

export default historyService;

