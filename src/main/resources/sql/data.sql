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

DELETE FROM `yearly_assets`;

INSERT INTO `yearly_assets` (`target_year`, `cash`, `securities`, `crypto`)
VALUES (2020, 201599, 64564, 0),
       (2021, 1400000, 1004564, 0),
       (2022, 4000000, 1604564, 100000),
       (2023, 1641599, 5834564, 158192),
       (2024, 1641599, 8304564, 260000),
       (2025, 1641599, 9834564, 448192);

DELETE FROM `monthly_assets`;

INSERT INTO `monthly_assets` (`target_year`, `target_month`, `cash`, `securities`, `crypto`)
VALUES (2022, 8, 1300000, 4584429, 200000),
       (2022, 9, 1300000, 4497235, 200000),
       (2022, 10, 1300000, 4846570, 200000),
       (2022, 11, 1300000, 4842383, 200000),
       (2022, 12, 1300000, 4957177, 200000),
       (2023, 1, 1300000, 5138890, 200000),
       (2023, 2, 1300000, 5298597, 200000),
       (2023, 3, 1300000, 5289308, 200000),
       (2023, 4, 1300000, 5462808, 200000),
       (2023, 5, 1300000, 5677298, 200000),
       (2023, 6, 1300000, 6219017, 200000),
       (2023, 7, 1300000, 5940934, 200000),
       (2023, 8, 1300000, 5938412, 200000),
       (2023, 9, 1300000, 6010927, 200000),
       (2023, 10, 1300000, 5999613, 200000),
       (2023, 11, 1300000, 6009363, 200000),
       (2023, 12, 1300000, 6088393, 200000),
       (2024, 1, 1300000, 7515626, 200000),
       (2024, 2, 1300000, 7821420, 200000),
       (2024, 3, 1300000, 8304739, 200000),
       (2024, 4, 1300000, 8575464, 200000),
       (2024, 5, 1300000, 8577947, 200000),
       (2024, 6, 1300000, 8546928, 200000),
       (2024, 7, 1300000, 8521272, 200000),
       (2024, 8, 1300000, 8540291, 200000),
       (2024, 9, 1300000, 8327160, 200000),
       (2024, 10, 1300000, 8289244, 200000),
       (2024, 11, 1300000, 8988841, 200000),
       (2024, 12, 1300000, 9152439, 200000),
       (2025, 1, 1300000, 9729714, 200000),
       (2025, 2, 1300000, 9621343, 200000),
       (2025, 3, 1300000, 9407125, 200000),
       (2025, 4, 1300000, 8881979, 200000),
       (2025, 5, 1300000, 9258892, 200000),
       (2025, 6, 1300000, 9960074, 200000),
       (2025, 7, 1300000, 9714108, 200000);

DELETE FROM `asset_goals`;

INSERT INTO `asset_goals` (`goal_amount`)
VALUES (15000000);

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
