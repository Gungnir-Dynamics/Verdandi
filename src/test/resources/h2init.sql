drop all objects;

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
                        username varchar(100) not null,
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


-- ==================== PROJECTS ====================
INSERT INTO project (name, description, deadline) VALUES
                                                      ('Website Redesign', 'Total redesign af virksomhedens hjemmeside med ny branding og moderne design', '2026-08-15'),
                                                      ('Mobil App Udvikling', 'Udvikling af en native mobilapp til både iOS og Android', '2026-10-01'),
                                                      ('ERP System Implementering', 'Implementering af nyt økonomi- og lagerstyringssystem', '2026-12-20'),
                                                      ('Markedsføringskampagne 2026', 'Stor sommerkampagne med fokus på sociale medier og influencers', '2026-07-10');


-- ==================== SUB PROJECTS ====================
INSERT INTO sub_project (name, description, project_id) VALUES
-- Project 1: Website Redesign
('Design fase', 'Wireframes, mockups og design af hele sitet', 1),
('Frontend udvikling', 'Udvikling af det visuelle lag med React og Tailwind', 1),
('Backend udvikling', 'API udvikling og database integration', 1),
('Test & QA', 'Test af funktionalitet og brugervenlighed', 1),

-- Project 2: Mobil App
('UI/UX Design', 'Design af brugergrænseflade og brugerrejser', 2),
('iOS Udvikling', 'Udvikling af iOS versionen (Swift)', 2),
('Android Udvikling', 'Udvikling af Android versionen (Kotlin)', 2),

-- Project 3: ERP System
('Analyse & Kravspecifikation', 'Indsamling og dokumentation af krav', 3),
('Systemkonfiguration', 'Opsætning og tilpasning af ERP-systemet', 3),
('Data migration', 'Overførsel af data fra gammelt system', 3);


-- ==================== TASKS ====================
INSERT INTO task (name, description, estimated_hours, sub_project_id) VALUES
-- Website - Design fase
('Lav wireframes', 'Low-fidelity wireframes af alle hoved sider', 35, 1),
('Design style guide', 'Farver, typografi og komponentbibliotek', 25, 1),
('High-fidelity mockups', 'Detaljerede designs i Figma', 48, 1),

-- Website - Frontend
('Opsæt React projekt', 'Initialisér med Vite, React og Tailwind', 16, 2),
('Lav responsivt navbar', 'Navigation til både mobil og desktop', 14, 2),
('Implementer homepage', 'Forside med hero section og features', 42, 2),
('Implementer about-side', 'About us og team side', 28, 2),

-- Website - Backend
('Opsæt backend struktur', 'Node.js/Express eller Laravel setup', 30, 3),
('Lav REST API endpoints', 'Bruger- og indholdsrelaterede endpoints', 55, 3),
('Database design & opsætning', 'ER-diagram og tabeloprettelse', 38, 3),

-- Mobil App - UI/UX
('Lav user flows', 'Komplette brugerrejser i Figma', 22, 5),
('High-fidelity designs', 'Detaljerede skærmbilleder til appen', 60, 5),

-- ERP - Analyse
('Interview med økonomiafdeling', 'Møder og kravsindsamling', 20, 8),
('Dokumentér krav', 'Skriv detaljeret kravsspecifikation', 35, 8),
('Analyse af nuværende system', 'Kortlægning af nuværende processer', 30, 8),
('Data migration plan', 'Plan for overførsel af data', 25, 10);

-- ==================== ROLES ====================
INSERT INTO role (role_name) VALUES
                                 ('admin'),
                                 ('user');


-- ==================== PROFILES ====================
INSERT INTO profile (username, email, password, hourly_rate, role_id) VALUES
                                                                          ('Anders Jensen', 'anders@example.com', 'anders123', 850, 1),
                                                                          ('Mette Larsen', 'mette@example.com', 'mette123', 650, 2),
                                                                          ('Jonas Nielsen', 'jonas@example.com', 'jonas123', 700, 2),
                                                                          ('Sofie Hansen', 'sofie@example.com', 'sofie123', 950, 1),
                                                                          ('Frederik Pedersen', 'frederik@example.com', 'frederik123', 600, 2),
                                                                          ('Emma Christensen', 'emma@example.com', 'emma123', 750, 2),
                                                                          ('Lucas Mortensen', 'lucas@example.com', 'lucas123', 500, 2),
                                                                          ('Ida Sørensen', 'ida@example.com', 'ida123', 680, 2);


-- ==================== ASSIGNMENTS ====================
INSERT INTO assignment (profile_id, project_id) VALUES

-- Website Redesign
(1, 1),
(2, 1),
(3, 1),
(7, 1),

-- Mobil App Udvikling
(1, 2),
(5, 2),
(6, 2),

-- ERP System
(4, 3),
(2, 3),
(8, 3),

-- Marketing Campaign
(4, 4),
(6, 4),
(7, 4);