create table machines_employees_can_use (
    machine_id bigint not null ,
    employee_id bigint not null,
    foreign key (machine_id) references machines (id),
    foreign key (employee_id) references employees (id)
);