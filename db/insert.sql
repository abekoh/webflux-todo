insert into
  task(
    created_on,
    updated_on,
    task_id,
    text,
    deadline,
    completed,
    deleted
  )
values
  (
    sysdate(),
    sysdate(),
    null,
    '御飯食べる',
    sysdate() + interval 1 day,
    false,
    false
  );

insert into
  task_list(
    created_on,
    updated_on,
    task_list_id,
    todo_rank,
    task_id
  )
values
  (
    sysdate(),
    sysdate(),
    null,
    1,
    (
      select
        task_id
      from
        task
      order by
        task_id
      limit
        1
    )
  );
