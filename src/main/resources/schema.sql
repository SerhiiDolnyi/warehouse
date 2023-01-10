DROP TABLE IF EXISTS partner CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS supplier CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS invoice CASCADE;
DROP TABLE IF EXISTS offer CASCADE;
DROP TABLE IF EXISTS registered_user CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users_roles CASCADE;

CREATE TABLE IF NOT EXISTS address (
						id SERIAL,
						postcode int,
						country VARCHAR (50),
                        city VARCHAR (50),
                        street VARCHAR (50),
                        office int,
                        PRIMARY KEY (id)
                        );

CREATE TABLE IF NOT EXISTS customer (
                        id SERIAL,
                        name VARCHAR (100) NOT NULL,
                        address_id int,
                        customer_rate int,
                        PRIMARY KEY (id),
                        FOREIGN KEY (address_id) REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE
                        );

CREATE TABLE IF NOT EXISTS supplier (
                        id SERIAL,
                        name VARCHAR (100) NOT NULL,
                        address_id int,
                        goods_return_rate int,
                        PRIMARY KEY (id),
                        FOREIGN KEY (address_id) REFERENCES address (id) ON UPDATE CASCADE ON DELETE CASCADE
                        );

CREATE TABLE IF NOT EXISTS item (
                        id SERIAL,
                        name VARCHAR(100) NOT NULL,
                        category VARCHAR(100),
                        description VARCHAR(100),
                        price DECIMAL(8,2),
                        amount int,
                        PRIMARY KEY (id)
                        );

CREATE TABLE IF NOT EXISTS invoice(
                        id SERIAL,
                        date DATE NOT NULL,
                        customer_id int,
                        item_id int,
                        price DECIMAL (8,2),
                        item_count int,
                        stage VARCHAR(20),
                        PRIMARY KEY (id),
                        FOREIGN KEY (customer_id) REFERENCES customer (id) ON UPDATE CASCADE ON DELETE CASCADE,
                        FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE CASCADE ON DELETE CASCADE
                        );

CREATE TABLE IF NOT EXISTS offer(
                        id SERIAL,
                        date DATE NOT NULL,
                        supplier_id int,
                        item_id int,
                        price DECIMAL (8,2),
                        item_count int,
                        stage VARCHAR(20),
                        PRIMARY KEY (id),
                        FOREIGN KEY (supplier_id) REFERENCES supplier (id) ON UPDATE CASCADE ON DELETE CASCADE,
                        FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE CASCADE ON DELETE CASCADE
                        );

CREATE TABLE IF NOT EXISTS registered_user (
                        id SERIAL,
                        name VARCHAR (50),
                        password VARCHAR,
                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roles (
                        id SERIAL,
                        name VARCHAR (50),
                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_roles (
                        user_id int REFERENCES registered_user(id) ON UPDATE CASCADE ON DELETE CASCADE,
                        role_id int REFERENCES roles(id) ON UPDATE CASCADE ON DELETE CASCADE,
                        PRIMARY KEY (user_id, role_id)
);
