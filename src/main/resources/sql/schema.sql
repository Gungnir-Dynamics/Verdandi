Drop database if exists Verdandi;
create database Verdandi;
use Verdandi;

create table project (
                         project_id int auto_increment,
                         name varchar(100) not null,
                         description varchar(5000),
                         created_date date default (current_date),
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
                     name varchar(100) not null,
                     description varchar(1500),
                     estimated_hours int,
                     sub_project_id int not null,
                     primary key (task_id),
                     foreign key (sub_project_id) references sub_project (sub_project_id) on delete cascade
);

create table role(
                     role_id int auto_increment,
                     role_name varchar(30) not null unique,
                     primary key(role_id)
);

create table profile(
                        profile_id int auto_increment,
                        name varchar(100) not null unique,
                        email varchar(100) not null unique,
                        password varchar(30) not null,
                        hourly_rate int not null,
                        role_id int,
                        primary key (profile_id),
                        foreign key (role_id) references role (role_id)
);



create table assignment(
                           profile_id int not null,
                           project_id int not null,
                           primary key (profile_id, project_id),
                           foreign key (profile_id) references profile (profile_id) on delete cascade,
                           foreign key (project_id) references project (project_id) on delete cascade
);
