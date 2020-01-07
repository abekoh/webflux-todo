DELETE FROM task;

DELETE FROM task_list;

ALTER TABLE task AUTO_INCREMENT = 1;

ALTER TABLE task_list AUTO_INCREMENT = 1;

INSERT INTO task_list (created_on,
                       updated_on,
                       task_list_id,
                       name,
                       priority_rank)
VALUES ('2020-01-01 16:10:53',
        '2020-01-01 16:10:59',
        NULL,
        'プライベート',
        1);
INSERT INTO task (created_on,
                  updated_on,
                  task_id,
                  text,
                  deadline,
                  completed,
                  deleted,
                  priority_rank,
                  task_list_id)
VALUES ('2020-01-01 00:00:00',
        '2020-01-01 00:00:00',
        NULL,
        'ご飯食べる',
        '2020-01-01 00:00:01',
        false,
        false,
        0,
        1),
       ('2020-01-01 00:00:00',
        '2020-01-01 00:00:00',
        NULL,
        '寝る',
        '2020-01-01 00:00:01',
        false,
        false,
        0,
        1),
       ('2020-01-01 00:00:00',
        '2020-01-01 00:00:00',
        NULL,
        '完了した',
        '2020-01-01 00:00:01',
        true,
        false,
        0,
        1),
       ('2020-01-01 00:00:00',
        '2020-01-01 00:00:00',
        NULL,
        '消した',
        '2020-01-01 00:00:01',
        false,
        true,
        0,
        1)
;
