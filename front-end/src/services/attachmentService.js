import apiService from "./apiService";

const postAttachment = (ticketId, attachment) => {

  return apiService.post(`/api/tickets/${ticketId}/attachments`, attachment);
};

const attachmentService = {
  postAttachment,
}

export default attachmentService;
