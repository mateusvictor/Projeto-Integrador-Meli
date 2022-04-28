INSERT INTO buyers VALUES('acb362e2-ebc9-4e06-92ee-790637e28dc3','email@gmail.com', 'Jessica', '123', 'ROLE_BUYER');
INSERT INTO products VALUES('3c1f01a6-a59f-42ce-8d19-16fbd40c10ff','fresh', 20.0, 15.0, 'peixe', 5.0);

INSERT INTO warehouses VALUES ('dd52b0c6-d9b9-4741-b251-4a8f31a97343', 'Melicidade');
INSERT INTO warehouse_managers VALUES ('c7e991d9-1a14-4077-a397-d4e4beb07dc1','email@gmail.com', 'Jessica', '123', 'ROLE_WAREHOUSEMANAGER', 'dd52b0c6-d9b9-4741-b251-4a8f31a97343');
INSERT INTO sections VALUES ('43e90fe-8754-498b-8f4d-6270c139be65', 30, 50, 'fresh', 'dd52b0c6-d9b9-4741-b251-4a8f31a97343', 'c7e991d9-1a14-4077-a397-d4e4beb07dc1');
INSERT INTO inbound_orders VALUES ('59c6e3d2-15d6-41c7-90b3-7c49c962cf74',now(),'43e90fe-8754-498b-8f4d-6270c139be65');

INSERT INTO batches
VALUES ('57d2c258-eeea-4710-999e-be59a59fa4f1',10, 10, '2022-05-27', 30, now(), 10, '59c6e3d2-15d6-41c7-90b3-7c49c962cf74', '3c1f01a6-a59f-42ce-8d19-16fbd40c10ff');