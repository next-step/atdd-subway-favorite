delete from section;
ALTER TABLE section ALTER COLUMN id RESTART WITH 1;

delete from line;
ALTER TABLE line ALTER COLUMN id RESTART WITH 1;

delete from station;
ALTER TABLE station ALTER COLUMN id RESTART WITH 1;

delete from member;
ALTER TABLE member ALTER COLUMN id RESTART WITH 1;

delete from favorite;
ALTER TABLE favorite ALTER COLUMN id RESTART WITH 1;