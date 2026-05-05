Drop database if exists Verdandi;
create database Verdandi;
use Verdandi;

create table project (
                         project_id int auto_increment,
                         name varchar(100) not null,
                         description varchar(5000),
                         createdDate date default (current_date),
                         deadline date,
                         primary key (project_id)
);

create table sub_project(
                            sub_project_id int auto_increment,
                            name varchar(100) not null,
                            description varchar(1500),
                            project_id int not null,
                            primary key (sub_project_id),
                            foreign key(project_id) references project (project_id) on delete cascade

);

create table task(
                     task_id int auto_increment,
                     name varchar(100),
                     description varchar(1500),
                     estimatedTime time,
                     sub_project_id int not null,
                     primary key (task_id),
                     foreign key (sub_project_id) references sub_project (sub_project_id) on delete cascade
);