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
INSERT INTO task (name, description, estimatedTime, sub_project_id) VALUES
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