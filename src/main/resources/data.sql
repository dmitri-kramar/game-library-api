INSERT INTO developer (name)
VALUES
    ('Bethesda'),
    ('CD Project Red'),
    ('Rockstar Games'),
    ('TakeTwo Interactive'),
    ('Ubisoft');

INSERT INTO genre (name)
VALUES
    ('Action'),
    ('Adventure'),
    ('Fighting'),
    ('FPS'),
    ('Horror'),
    ('Racing'),
    ('RPG'),
    ('Sports'),
    ('Strategy');

INSERT INTO platform (name)
VALUES
    ('PC'),
    ('Playstation'),
    ('Xbox');

INSERT INTO game (description, release_date, title, developer_id)
VALUES
    ('Futuristic open-world RPG.','2023-11-15','Cyberpunk 2077',3),
    ('Doom on Mars','2004-07-17','Doom III',1),
    ('Welcome to Los Santos!','2015-03-12','GTA 5',4);

INSERT INTO role (name)
VALUES
    ('USER'),
    ('ADMIN');

INSERT INTO "user" (password, username, role_id)
VALUES
    ('$2a$12$333kCKkWMnqaHAOEm/B6VeZO1ELmzS6O4jjONrpKR2EeahYLgjeaa','admin',2),
    ('$2a$12$PAEDPcPzeCSNK6QHjiVCouxFyo/eX3GdD98YyJqo5Jcbld2ILu//G','user',1);

INSERT INTO game_genre
VALUES
    (1,1),
    (2,1),
    (3,1),
    (1,2),
    (2,2),
    (3,3),
    (1,4),
    (2,4);

INSERT INTO game_platform
VALUES
    (1,1),
    (2,1),
    (3,1),
    (1,2),
    (2,3);