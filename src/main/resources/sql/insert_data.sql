INSERT INTO user (uuid, version, firstName, lastName, registrationDate)
VALUES (0x70E4A7B16B004285819E3B41CE405FF3, 0, 'admin', 'admin', '2021-02-08 00:59:07');

INSERT INTO securityuser (uuid, version, username, password, enabled, accountNonExpired, accountNonLocked, credentialsNonExpired, userId, email)
VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 0, 'admin', '$2a$10$/e5hookggECzq24SQ8zr/ODFH/Bp7FD0Nc2VqLFw7WkSItEAWPaZ6', 'Y', 'Y', 'Y', 'Y', 0x70E4A7B16B004285819E3B41CE405FF3, null);

INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_DELETE_ISSUE');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_DELETE_TICKET');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_DELETE_USER');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_EDIT_ISSUE');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_EDIT_TICKET');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_EDIT_TICKET_STATE');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_EDIT_USER');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_LIST_ISSUES');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_LIST_TICKETS');
INSERT INTO securityuser_authority (secUserId, authority) VALUES (0x4B5EA4C385A34AF3A6531809EA552376, 'ROLE_LIST_USERS');