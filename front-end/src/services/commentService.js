import apiService from "./apiService";

const postComment = (ticketId, comment) => {

  return apiService.post(`/api/tickets/${ticketId}/comments`, comment);
};

const commentService = {
  postComment,
}

export default commentService;


