INSERT INTO developer (name)
VALUES
    ('Bethesda Game Studios'),
    ('CD Projekt Red'),
    ('Rockstar Games'),
    ('Valve'),
    ('Ubisoft');

INSERT INTO genre (name)
VALUES
    ('Action'),
    ('RPG'),
    ('Adventure'),
    ('Shooter'),
    ('Stealth'),
    ('Simulation'),
    ('Racing'),
    ('Platformer'),
    ('Strategy');

INSERT INTO platform (name)
VALUES
    ('PC'),
    ('PlayStation 5'),
    ('Xbox Series X');

INSERT INTO game (description, release_date, title, developer_id)
VALUES
    ('A post-apocalyptic open-world RPG.', '2015-11-10', 'Fallout 4', 1),
    ('Fantasy open-world RPG.', '2015-05-19', 'The Witcher 3: Wild Hunt', 2),
    ('An epic Western action game.', '2018-10-26', 'Red Dead Redemption 2', 3),
    ('Tactical shooter and bomb defusal.', '2012-08-21', 'Counter-Strike: Global Offensive', 4),
    ('Historical action-adventure.', '2020-11-10', 'Assassin’s Creed Valhalla', 5);

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
    (1,2),
    (1,1),
    (2,2),
    (2,3),
    (3,1),
    (3,3),
    (4,4),
    (5,1),
    (5,5);

INSERT INTO game_platform
VALUES
    (1,1),
    (1,2),
    (2,1),
    (2,2),
    (3,1),
    (3,3),
    (4,1),
    (5,1),
    (5,2);