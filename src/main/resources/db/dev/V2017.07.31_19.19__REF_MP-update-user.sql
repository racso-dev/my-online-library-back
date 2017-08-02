
UPDATE user SET profile = 1 WHERE id = 1;
UPDATE user SET profile = 2 WHERE id IN (2,3,4);
UPDATE user SET profile = 3 WHERE id = 7;
UPDATE user SET profile = 4 WHERE id IN (5,6);

