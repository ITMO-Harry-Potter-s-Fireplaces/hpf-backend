insert into roles (name) values ('Администратор'); -- ID: 0
insert into roles (name) values ('Модератор'); -- ID: 1
insert into roles (name) values ('Пользователь'); -- ID: 2

insert into users (name, surname, middle_name, email, password, role_id, date_birth, active)
values ('admin', 'admin', 'admin', 'admin@admin.com', '202cb962ac59075b964b07152d234b70', 0, '2020-01-01' ,true);
