DELETE FROM `authorities`
WHERE `username` = 'admin';

DELETE FROM `users`
WHERE `username` = 'admin';

INSERT INTO `users`
(`username`, `password`, `enabled`) VALUES
(   'admin', '{bcrypt}$2a$10$kxhJnySfXtAL6xjlVks36e.NkqIiXCSUHy2Z2zT8HO8jETJ/t6YwK', TRUE);

INSERT INTO `authorities`
(`username`, `authority`) VALUES
(   'admin', 'ROLE_USER');

DELETE FROM `annual_assets`;

INSERT INTO `annual_assets` (`target_year`, `cash`, `securities`, `crypto`)
VALUES (2020, 201599, 64564, 0),
       (2021, 1400000, 1004564, 0),
       (2022, 4000000, 1604564, 100000),
       (2023, 1641599, 5834564, 158192),
       (2024, 1641599, 8304564, 260000),
       (2025, 1641599, 9834564, 448192);

DELETE FROM `monthly_assets`;

INSERT INTO `monthly_assets` (`target_year`, `target_month`, `cash`, `securities`, `crypto`)
VALUES (2024, 12, 1641599, 8304564, 260000);

--DELETE FROM `authorities`
--WHERE `username` = 'guest';
--
--DELETE FROM `users`
--WHERE `username` = 'guest';
--
--INSERT INTO `users`
--(`username`, `password`, `enabled`) VALUES
--(   'guest', '{bcrypt}$2a$10$KY4iiHlRc8bfj.lQveu8DO.FgNLnCpon8lOPr4D3kHxwgISTUngTm', TRUE);
--
--INSERT INTO `authorities`
--(`username`, `authority`) VALUES
--(   'guest', 'ROLE_USER');
