insert into employees (name, qualification) values ('Nagy Roland', 'MILLING');
insert into employees (name, qualification) values ('Iglói Péter', 'TURNING');
insert into employees (name, qualification) values ('Kovács István', 'GRINDING');
insert into employees (name, qualification) values ('Takács Vanda', 'MILLING');
insert into employees (name, qualification) values ('Szalai Zorka', 'TURNING');

insert into machines (machine_name, machine_type) values ('Hermle', 'MILL');
insert into machines (machine_name, machine_type) values ('Mikron', 'MILL');
insert into machines (machine_name, machine_type) values ('Okamoto', 'GRINDER');
insert into machines (machine_name, machine_type) values ('EMCO', 'LATHE');

insert into jobs (cost, customer, dead_line, estimated_machining_hours, order_date, order_type, spent_machining_hours, job_status)
values (100, 'BPW Hungary', '2024-05-15', 200, '2023-01-05', 'STANDARD', 0, 'NEW');
insert into jobs (cost, customer, dead_line, estimated_machining_hours, order_date, order_type, spent_machining_hours, job_status)
values (100, 'Continental Hungary', '2025-06-12', 200, '2023-02-02', 'PRIORITY', 0, 'NEW');
insert into jobs (cost, customer, dead_line, estimated_machining_hours, order_date, order_type, spent_machining_hours, job_status)
values (100, 'Euroform Kft', '2023-11-10', 200, '2023-03-20', 'DISCOUNTED', 0, 'RUNNING');