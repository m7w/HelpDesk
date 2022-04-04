import apiService from "./apiService";

const getFeedback = (ticketId) => {
  return apiService.get(`/api/tickets/${ticketId}/feedback`);
};

const postFeedback = (ticketId, feedback) => {
  return apiService.post(`/api/tickets/${ticketId}/feedback`, feedback);
};

const feedbackService = {
  getFeedback,
  postFeedback,
}

export default feedbackService;
