create table if not exists task_list
(
    created_on    datetime      not null,
    updated_on    datetime      not null,
    task_list_id  int auto_increment
        primary key,
    name          varchar(2000) not null,
    priority_rank int           not null
);

create table if not exists task
(
    created_on    datetime             not null,
    updated_on    datetime             not null,
    task_id       int auto_increment
        primary key,
    text          varchar(4000)        null,
    deadline      datetime             null,
    completed     boolean default false not null,
    deleted       boolean default false not null,
    priority_rank int                  not null,
    task_list_id  int                  null,
    constraint task_task_list_task_list_id_fk
        foreign key (task_list_id) references task_list (task_list_id)
);
