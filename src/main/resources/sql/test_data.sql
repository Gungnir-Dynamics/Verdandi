-- ==================== PROJECTS ====================
INSERT INTO project (name, description, deadline) VALUES
                                                      ('Website Redesign', 'Total redesign af virksomhedens hjemmeside med ny branding', '2026-08-15'),
                                                      ('Mobil App Udvikling', 'Udvikling af en ny mobilapp til både iOS og Android', '2026-10-01'),
                                                      ('ERP System Implementering', 'Implementering af nyt økonomi- og lagerstyringssystem', '2026-12-20'),
                                                      ('Markedsføringskampagne', 'Sommer kampagne 2026 med fokus på sociale medier', '2026-07-10');


-- ==================== SUB PROJECTS ====================
INSERT INTO sub_project (name, description, estimatedTime, project_id) VALUES
-- Project 1: Website Redesign
('Design fase', 'Wireframes, mockups og design af hele sitet', '2026-06-15', 1),
('Frontend udvikling', 'Udvikling af det visuelle lag (React)', '2026-07-10', 1),
('Backend udvikling', 'API og database integration', '2026-07-25', 1),

-- Project 2: Mobil App
('UI/UX Design', 'Design af brugergrænsefladen', '2026-06-20', 2),
('iOS Udvikling', 'Udvikling til iPhone og iPad', '2026-08-15', 2),
('Android Udvikling', 'Udvikling til Android enheder', '2026-08-20', 2),

-- Project 3: ERP System
('Analyse & Krav', 'Indsamling af krav fra afdelingerne', '2026-06-10', 3),
('Konfiguration', 'Opsætning og tilpasning af systemet', '2026-09-01', 3);


-- ==================== TASKS ====================
INSERT INTO task (name, description, estimatedTime, sub_project_id) VALUES
-- Sub project 1.1 (Design fase)
('Lav wireframes', 'Lav low-fidelity wireframes af alle hoved sider', '2026-05-20', 1),
('Design style guide', 'Farver, typografi og komponent bibliotek', '2026-05-25', 1),

-- Sub project 1.2 (Frontend)
('Opsæt React projekt', 'Initialisér projektet med Vite + React', '2026-06-05', 2),
('Lav responsivt navbar', 'Navigation der virker på mobil og desktop', '2026-06-12', 2),

-- Sub project 2.1 (UI/UX Design)
('Lav user flows', 'Tegn komplette brugerrejser', '2026-06-10', 4),
('High-fidelity mockups', 'Detaljerede designs i Figma', '2026-06-18', 4),

-- Sub project 3.1 (Analyse)
('Interview med økonomiafdeling', 'Møder og kravsindsamling', '2026-05-15', 7),
('Dokumentér krav', 'Skriv kravsspecifikation', '2026-05-22', 7);