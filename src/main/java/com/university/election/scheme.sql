-- Drop tables if they exist
DROP TABLE IF EXISTS candidates CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS elections CASCADE;

-- Create elections table
CREATE TABLE elections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create candidates table
CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    faculty VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL CHECK (year_of_study >= 2 AND year_of_study <= 4),
    campaign TEXT,
    election_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE
);

-- Create students table
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    student_id VARCHAR(50) UNIQUE NOT NULL,
    faculty VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL CHECK (year_of_study >= 1 AND year_of_study <= 4),
    has_voted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO elections (name, start_date, end_date, academic_year) VALUES
    ('University President Election 2026', '2026-01-10', '2026-01-19', '2025-2026'),
    ('University President Election 2027', '2027-01-10', '2027-01-19', '2026-2027');

INSERT INTO candidates (name, faculty, year_of_study, campaign, election_id) VALUES
    ('Bekbolat Aruzhan', 'Software Engineering', 2, 'New learning platforms for better education', 1),
    ('Alikhan Nursultan', 'Computer Science', 3, 'Improved campus facilities and student support', 1),
    ('Aigerim Samal', 'Information Systems', 4, 'Stronger student voice in university decisions', 1);

INSERT INTO students (name, student_id, faculty, year_of_study, has_voted) VALUES
    ('Arguan Bakikair', 'S001', 'Software Engineering', 1, FALSE),
    ('Dias Yerlan', 'S002', 'Computer Science', 2, TRUE),
    ('Kamila Aisha', 'S003', 'Information Systems', 3, FALSE),
    ('Timur Bolat', 'S004', 'Software Engineering', 4, TRUE);