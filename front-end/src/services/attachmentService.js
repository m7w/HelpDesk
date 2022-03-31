import apiService from "./apiService";

const getAttachmentsInfo = (ticketId) => {

  return apiService.get(`/api/tickets/${ticketId}/attachments`);
};

const getAttachment = (ticketId, id) => {

  return apiService.get(`/api/tickets/${ticketId}/attachments/${id}`, {
    responseType: "blob",
  });
};

const postAttachment = (ticketId, attachment) => {

  return apiService.post(`/api/tickets/${ticketId}/attachments`, attachment);
};

const deleteAttachment = (ticketId, id) => {

  return apiService.delete(`/api/tickets/${ticketId}/attachments/${id}`);
};

const attachmentService = {
  getAttachmentsInfo,
  getAttachment,
  postAttachment,
  deleteAttachment,
}

export default attachmentService;
