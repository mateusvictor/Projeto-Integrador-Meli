USE pi_db_test;

-- Creating user
INSERT INTO users (id, name, email, password)
VALUES ("1", "admin", "gustavo@admin.com", "admin");

-- Setting roles
-- INSERT INTO roles
-- VALUES
-- ("2", 0),
-- ("2", 1),
-- ("2", 2),
-- ("2", 3);

-- Creating warehouse
INSERT INTO warehouses
VALUES ("10", "Warehouse Report Test", "1");

-- Creating sections
INSERT INTO sections (id, warehouse_id)
VALUES
("1", "10"),
("2", "10");

-- Creating products
INSERT INTO products(id, category, is_active)
VALUES
("1", "FF", true),
("2", "RF", true);

-- Creating inbound order
INSERT INTO inbound_orders (id, section_id) VALUES("1", "1");

-- Creating batches
INSERT INTO batches (id, current_quantity, due_date, inbound_order_id, product_id)
VALUES
("1", "10", "2022-05-20", "1", "1"),
("2", "5", "2022-05-10", "1", "1"),
("3", "15", "2022-05-07", "1", "2"),
("4", "20", "2022-05-25", "1", "2");
