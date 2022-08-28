-- Precreate users
INSERT INTO USERS(first_name, last_name, role_id, email, password) VALUES ('Lenny', 'Leonart', 0, 'user1_mogilev@yopmail.com', '$2a$10$JzM7o.h9hKaeq65DBJv3qOO8IKDbB2D.XJNrbAW.G6AbC6yp0QcIy');
INSERT INTO USERS(first_name, last_name, role_id, email, password) VALUES ('Carl', 'Carlson', 0, 'user2_mogilev@yopmail.com', '$2a$10$OpiLgVae9BFCdADylrHu..PGk8RrKYoZ8QTbknnVyO3R9YvK4HABe');
INSERT INTO USERS(first_name, last_name, role_id, email, password) VALUES ('Seymour', 'Skinner', 1, 'manager1_mogilev@yopmail.com', '$2a$10$ld1EVcYscZYXfUOmS7SHeuQGnIFT1cK8ta76OazXwAe.9iOJY8X7K');
INSERT INTO USERS(first_name, last_name, role_id, email, password) VALUES ('Ned', 'Flanders', 1, 'manager2_mogilev@yopmail.com', '$2a$10$bnYOu5mGhNebmtckVqJZz.knps/FW/PEq9FJaikF1nC5ey1zs2g.e');
INSERT INTO USERS(first_name, last_name, role_id, email, password) VALUES ('Homer', 'Simpson', 2, 'engineer1_mogilev@yopmail.com', '$2a$10$XMiturr4FhYxGov9y5ibtuhJR9unIYXp2xqzzr8idHtAqPs.ABXAO');
INSERT INTO USERS(first_name, last_name, role_id, email, password) VALUES ('Frank', 'Grimes', 2, 'engineer2_mogilev@yopmail.com', '$2a$10$pBtyYp4ebCrHvazZ8F72JuZej.NFO.e3l3SjZraoGDM39rX6wie/.');

-- Precreate categories
INSERT INTO CATEGORIES(name) VALUES ('Application & Services');
INSERT INTO CATEGORIES(name) VALUES ('Benefits & Paper Work');
INSERT INTO CATEGORIES(name) VALUES ('Hardware & Software');
INSERT INTO CATEGORIES(name) VALUES ('People Management');
INSERT INTO CATEGORIES(name) VALUES ('Security & Access');
INSERT INTO CATEGORIES(name) VALUES ('Workplaces & Facilities');



-- Precreate some tickets
INSERT INTO TICKETS(name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id) VALUES ('Ticket1', 'There should be some description, but Im lazy', '2022-01-30', '2022-02-28', 5, 1, 'NEW', 5, 1, 3);
INSERT INTO TICKETS(name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id) VALUES ('Ticket2', 'Structural linguistics is defined as a study of language based on the theory that language is a structured system of formal units such as sentences and syntax. An example of structural linguistics is phonetics', '2022-02-03', '2022-03-01', 6, 3, 'IN_PROGRESS', 2, 3, 4);
INSERT INTO TICKETS(name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id) VALUES ('Ticket3', 'WTF', '2022-02-05', '2022-03-02', 5, 2, 'APPROVED', 1, 2, 4);
INSERT INTO TICKETS(name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id) VALUES ('Ticket4', 'Ticket4 lacus vestibulum sed arcu non odio euismod lacinia at quis risus sed vulputate odio ut enim blandit volutpat maecenas volutpat', '2022-02-15', '2022-03-12', 5, 2, 'APPROVED', 1, 2, 4);
INSERT INTO TICKETS(name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id) VALUES ('Ticket5', 'Ticket5 dictum non consectetur a erat nam at lectus urna duis convallis convallis tellus id interdum velit laoreet id donec ultrices', '2022-02-16', '2022-03-11', 6, 1, 'NEW', 5, 0, 4);
INSERT INTO TICKETS(name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id) VALUES ('Ticket6', 'Ticket6 The simplest way to implement pagination is to use the Java Query Language - create a query and configure it via setMaxResults and setFirstResult', '2022-02-11', '2022-03-04', 6, 4, 'APPROVED', 3, 3, 5);

-- Precreate comment
INSERT INTO COMMENTS(user_id, text, date, ticket_id) VALUES (1, 'Grimes', '2022-03-30', 1);
INSERT INTO COMMENTS(user_id, text, date, ticket_id) VALUES (1, 'Check comments to tickets', '2022-03-30', 2);

-- Precreate history
INSERT INTO HISTORY(ticket_id, date, action, user_id, description) VALUES (2, '2022-03-20', 'Ticket is edited', 1, 'Ticket is edited');
INSERT INTO HISTORY(ticket_id, date, action, user_id, description) VALUES (2, '2022-03-21', 'Ticket status is changed', 3, 'Ticket status is changed from X to Y');
