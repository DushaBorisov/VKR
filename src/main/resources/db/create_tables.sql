create table role
(
    id   bigserial primary key,
    name varchar(255)
);

create table users
(
    id       bigserial primary key,
    password varchar(255),
    username varchar(255),
    role_id  bigint references role (id)
);

create table students
(
    student_id           bigserial primary key,
    user_id              bigint references users (id),
    student_name         varchar(255),
    student_surname      varchar(255),
    student_email        varchar(255),
    student_phone_number varchar(255),
    student_resume       varchar(7000),
    desired_position     varchar(255),
    desired_salary       integer,
    desired_employment   varchar(255),
    search_status        varchar(255),
    course_of_study      varchar(255),
    experience           integer
);

create table companies
(
    company_id           bigserial primary key,
    user_id              bigint references users (id),
    company_name         varchar(255),
    company_description  varchar(7000),
    company_email        varchar(255),
    company_phone_number varchar(255)
);

create table jobs
(
    job_id                  bigserial primary key,
    company_id              bigint references companies (company_id),
    job_title               varchar(255),
    job_description         varchar(7000),
    job_salary              integer,
    job_status              varchar(255),
    job_employment          varchar(50),
    job_required_experience varchar(50)
);

create table job_responses
(
    response_id          bigserial primary key,
    student_id           bigint references students (student_id),
    company_id           bigint references companies (company_id),
    response_description varchar(7000),
    response_date        timestamp
);