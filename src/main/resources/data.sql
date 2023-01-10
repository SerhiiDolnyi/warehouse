delete from invoice;
delete from offer;
delete from supplier;
delete from customer;
delete from address;
delete from item;
delete from users_roles;
delete from roles;
delete from registered_user;

-- INSERT   ADDRESS
insert into address (postcode, country, city, street, office) values (03189, 'Ukraine', 'Kyiv', 'Shevchenka', 100);
insert into address (postcode, country, city, street, office) values (10200, 'USA', 'Detroit', 'Shevchenka', 100);
insert into address (postcode, country, city, street, office) values (02330, 'China', 'Beijing', 'Shevchenka', 100);
insert into address (postcode, country, city, street, office) values (10000, 'Poland', 'Krakiw', 'Shevchenka', 100);
insert into address (postcode, country, city, street, office) values (03189, 'Italy', 'Milan', 'Shevchenka', 100);
insert into address (postcode, country, city, street, office) values (03189, 'Moldova', 'Kisheniv', 'Shevchenka', 100);
insert into address (postcode, country, city, street, office) values (03189, 'Canada', 'Toronto', 'Shevchenka', 1);
insert into address (postcode, country, city, street, office) values (03189, 'Japan', 'Tokio', 'Shevchenka', 2);
insert into address (postcode, country, city, street, office) values (03189, 'USA', 'NY', 'Shevchenka', 3);
insert into address (postcode, country, city, street, office) values (03189, 'Japan', 'Tokio', 'John', 4);
insert into address (postcode, country, city, street, office) values (03189, 'CANADA', 'Toronto', 'Smith', 5);

-- INSERT INTO ITEM
insert into item (name, category, description, price, amount) values ('Java Start', 'book', 'Shieldt Introduction to Java', 30, 2);
insert into item (name, category, description, price, amount) values ('SQL from scratch', 'book', 'John SQL from scratch', 25, 5);
insert into item (name, category, description, price, amount) values ('smart TV', 'TV', 'TV with smart function', 5000, 3);
insert into item (name, category, description, price, amount) values ('Xiaomi Redmi8', 'smartphone', 'Smartphone Xiaomi Redmi8', 4000, 4);
insert into item (name, category, description, price, amount) values ('Iphone 10X', 'smartphone', 'Iphone smartphone', 20000, 9);
insert into item (name, category, description, price, amount) values ('Dell Inspirion 5433', 'notebook', 'notebook Dell Inspirion 5433', 25000, 4);
insert into item (name, category, description, price, amount) values ('Asus Zenbook', 'notebooke', 'notebook Asus zenbook', 35000, 5);

--INSERT INTO SUPPLIER
insert into supplier (name, address_id, goods_return_rate) values ('Amazon', 1, 7);
insert into supplier (name, address_id, goods_return_rate) values ('Dell', 2, 5);
insert into supplier (name, address_id, goods_return_rate) values ('Xiaomi', 3, 7);

--INSERT INTO CUSTOMER
insert into customer (name, address_id, customer_rate) values ('School#17', 4, 2);
insert into customer (name, address_id, customer_rate) values ('Center', 5, 6);
insert into customer (name, address_id, customer_rate) values ('SRC', 6, 4);

--INSERT INTO INVOICE
insert into invoice (date, customer_id, item_id, price, item_count, stage) VALUES ('2022-01-01', 1, 1, 40, 1, 'Registered');
insert into invoice (date, customer_id, item_id, price, item_count, stage) VALUES ('2022-02-02', 2, 2, 30, 1, 'Registered');
insert into invoice (date, customer_id, item_id, price, item_count, stage) VALUES ('2022-03-03', 3, 3, 6000, 1, 'Fulfilled');

--INSERT INTO OFFER
insert into offer (date, supplier_id, item_id, price, item_count, stage) VALUES ('2022-01-01', 1, 1, 30, 1, 'Fulfilled');
insert into offer (date, supplier_id, item_id, price, item_count, stage) VALUES ('2022-02-02', 2, 2, 25, 2, 'Fulfilled');
insert into offer (date, supplier_id, item_id, price, item_count, stage) VALUES ('2022-03-03', 3, 3, 5000, 3, 'Registered');

    --INSERT INTO roles
    insert into roles (name) values ('ADMIN');
    insert into roles (name) values ('STAFF');
    insert into roles (name) values ('PARTNER');
    insert into roles (name) values ('USER');

    --INSERT INTO users
    insert into registered_user (name, password) values ('admin', '123');
    insert into registered_user (name, password) values ('staff', '123');
    insert into registered_user (name, password) values ('partner', '123');
    insert into registered_user (name, password) values ('user', '123');

    -- users_roles
    insert into users_roles (user_id, role_id) VALUES (1, 1);
    insert into users_roles (user_id, role_id) VALUES (2, 2);
    insert into users_roles (user_id, role_id) VALUES (3, 3);
    insert into users_roles (user_id, role_id) VALUES (4, 4);
