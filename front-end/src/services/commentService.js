import apiService from "./apiService";

const getComments = (ticketId) => {

  return apiService.get(`/api/tickets/${ticketId}/comments`);
}

const postComment = (ticketId, comment) => {

  return apiService.post(`/api/tickets/${ticketId}/comments`, comment);
};

const commentService = {
  getComments,
  postComment,
}

export default commentService;


