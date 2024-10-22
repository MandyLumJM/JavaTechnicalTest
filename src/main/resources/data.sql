CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    wallet_balance DECIMAL(15, 2) NOT NULL DEFAULT 50000.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency VARCHAR(10) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    user_Id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);



-- Insert into user table
INSERT INTO app_user (id, email, username, wallet_balance)
VALUES (1,  'john.doe@example.com','John Doe', 50000);



-- Insert into wallet table
INSERT INTO wallet (id, balance, currency, user_id)
VALUES (1, 50000, 'USDT', 1);
INSERT INTO wallet (id, balance, currency, user_id)
VALUES (2, 0, 'BTC', 1);
INSERT INTO wallet (id, balance, currency, user_id)
VALUES (3, 0, 'ETH', 1);