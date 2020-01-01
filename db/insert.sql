INSERT INTO
  todo.task (
    created_on,
    updated_on,
    task_id,
    text,
    deadline,
    completed,
    deleted,
    priority_rank,
    task_list_id
  )
VALUES
  (
    '2020-01-01 05:37:10',
    '2020-01-01 05:37:10',
    NULL,
    '御飯食べる',
    '2020-01-02 05:37:10',
    0,
    0,
    0,
    1
  );
INSERT INTO
  todo.task_list (
    created_on,
    updated_on,
    task_list_id,
    name,
    priority_rank
  )
VALUES
  (
    '2020-01-01 16:10:53',
    '2020-01-01 16:10:59',
    NULL,
    'プライベート',
    1
  );
