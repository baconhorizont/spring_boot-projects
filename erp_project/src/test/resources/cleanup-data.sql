delete from spent_machining_hours_by_type;
delete from jobs_required_machines;
delete from machines_employees_can_use;
delete from employees;
delete from machines;
delete from jobs;

ALTER TABLE employees AUTO_INCREMENT = 1;
ALTER TABLE machines AUTO_INCREMENT = 1;
ALTER TABLE jobs AUTO_INCREMENT = 1;