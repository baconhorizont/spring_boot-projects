create table jobs_required_machines (
    job_id bigint not null ,
    machine_id bigint not null,
    foreign key (job_id) references jobs (id),
    foreign key (machine_id) references machines (id)
);