create table jobs (
    id bigint not null auto_increment,
    order_date date,
    dead_line date,
    order_type varchar (255),
    estimated_machining_hours int,
    cost double,
    spent_machining_hours int,
    job_status varchar (255),
    customer_id bigint not null,
    primary key (id),
    foreign key (customer_id) references customers(id)
);
