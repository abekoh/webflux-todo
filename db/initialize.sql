create table todo.task (
  created_on datetime not null,
  updated_on datetime not null,
  task_id int auto_increment primary key,
  text varchar(4000) null,
  deadline datetime null,
  completed tinyint(1) default 0 not null,
  deleted tinyint(1) default 0 not null
);
create table todo.task_list (
  created_on datetime not null,
  updated_on datetime not null,
  task_list_id int auto_increment primary key,
  todo_rank int not null,
  task_id int not null,
  constraint task_list_task_task_id_fk foreign key (task_id) references todo.task (task_id) on update cascade on delete cascade
);
