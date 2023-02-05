INSERT INTO Organization (id, name) VALUES (1, 'Apple');
INSERT INTO Organization (id, name) VALUES (2, 'Microsoft');
INSERT INTO Organization (id, name) VALUES (3, 'Google');

INSERT INTO Department (id, name, organization_id) VALUES (1, 'Development', 1);
INSERT INTO Department (id, name, organization_id) VALUES (2, 'Development', 2);
INSERT INTO Department (id, name, organization_id) VALUES (3, 'Development', 3);

INSERT INTO Employee (id, first_name, last_name, position, salary, age, organization_id, department_id)
VALUES (1111, 'Viet', 'Mai', 'Backend Developer', 30000000, 23, 1, 2);

INSERT INTO Employee (id, first_name, last_name, position, salary, age, organization_id, department_id)
VALUES (22222, 'Mai', 'Viet', 'Mobile Developer', 22000000, 23, 2, 1);

INSERT INTO Employee (id, first_name, last_name, position, salary, age, organization_id, department_id)
VALUES (33333, 'Mike', 'Viet', 'Android Developer', 10000000, 23, 3, 1);

INSERT INTO Employee (id, first_name, last_name, position, salary, age, organization_id, department_id)
VALUES (4444, 'Mike', 'Vie', 'Technical Manager', 75000000, 35, 3, 2);