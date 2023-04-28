create table jobs (
    id bigint not null auto_increment,
    customer varchar (255),
    order_date date,
    dead_line date,
    order_type varchar (255),
    estimated_machining_hours int,
    cost double,
    spent_machining_hours int,
    job_status varchar (255),
    primary key (id)
);
