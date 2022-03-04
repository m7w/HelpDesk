export const COMMENTS_TABLE_COLUMNS = [
  { id: "date", label: "Date" },
  { id: "user", label: "User" },
  { id: "comment", label: "Comment" },
];

export const HISTORY_TABLE_COLUMNS = [
  { id: "date", label: "Date" },
  { id: "user", label: "User" },
  { id: "action", label: "Action" },
  { id: "description", label: "Description" },
];

export const TICKETS_TABLE_COLUMNS = [
  { id: "id", db_name: "t.id", label: "ID", align: "left" },
  { id: "name", db_name: "t.name", label: "Name", align: "left" },
  { id: "resolutionDate", db_name: "t.desiredResolutionDate", label: "Desired Date" },
  { id: "urgency", db_name: "t.urgency", label: "Urgency", align: "left" },
  { id: "status", db_name: "t.state", label: "Status", align: "left" },
  { id: "action", db_name: "t.action", label: "Action", align: "center" },
];
