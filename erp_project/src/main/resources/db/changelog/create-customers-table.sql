create table customers (
    id bigint not null auto_increment,
    name varchar(255),
    vat_number varchar(255),
    registration_date date,
    currency varchar(255),
    country varchar(255),
    postal_code varchar(255),
    city varchar(255),
    street varchar(255),
    street_number varchar(255),
    primary key (id)
);