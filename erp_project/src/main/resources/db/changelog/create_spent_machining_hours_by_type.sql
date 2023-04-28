create table spent_machining_hours_by_type (
    job_id bigint not null,
    hours_sum bigint,
    machine_type varchar(255) not null,
    primary key (job_id, machine_type),
    foreign key (job_id) references jobs (id)
);

