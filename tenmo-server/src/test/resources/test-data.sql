BEGIN TRANSACTION;

INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user1','user1','ROLE_USER');
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user2','user2','ROLE_USER');
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user3','user3','ROLE_USER');

INSERT INTO tenmo_account (user_id, balance) VALUES (1001, 1000.00);
INSERT INTO tenmo_account (user_id, balance) VALUES (1002, 500.00);
INSERT INTO tenmo_account (user_id, balance) VALUES (1003, 100.00);

INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (2, 2, 2001, 2002, 100.00);

INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (1, 1, 2001, 2002, 100.00);
INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (1, 2, 2001, 2002, 100.00);
INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (1, 3, 2001, 2002, 100.00);

INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (2, 2, 2001, 2003, 100.00);



COMMIT TRANSACTION;
