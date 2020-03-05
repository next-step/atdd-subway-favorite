INSERT INTO USER (id, name, email, password) VALUES ('1', '김상구', 'sgkim94@github.com', 'password');
INSERT INTO STATION (id, name) VALUES ('1', '강남역'), ('2', '역삼역');
INSERT INTO LINE (id, name, start_time, end_time, interval_time) VALUES ('1', '2호선', '00:00', '23:30', '10');
INSERT INTO EDGE (id, line_id, source_station_id, target_station_id, distance) VALUES ('1', '1', '1', '2', '10');
INSERT INTO FAVORITE (id, user_id, item_id, type) values ('1', '1', '1', 'edge');

