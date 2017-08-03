
UPDATE user SET profile = 1 WHERE login = 'admin';
UPDATE user SET profile = 2 WHERE login IN ('super','super2','super3');
UPDATE user SET profile = 3 WHERE login = 'user3';
UPDATE user SET profile = 4 WHERE login IN ('user','user2');

