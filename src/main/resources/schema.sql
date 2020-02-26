create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    start_time time not null,
    end_time time not null,
    interval_time int,
    primary key(id)
);

create table if not exists EDGE
(
    id bigint auto_increment not null,
    line_id bigint not null,
    source_station_id bigint not null,
    target_station_id bigint not null,
    distance int,
    primary key(id)
);

create table if not exists USER
(
    id bigint auto_increment not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    name varchar(255) not null,
    primary key(id)
);

create table if not exists FAVORITE_STATION
(
    id bigint auto_increment not null,
    owner bigint not null,
    station_Id bigint not null,
    primary key(id)
);

create table if not exists FAVORITE_PATH
(
    id bigint auto_increment not null,
    owner bigint not null,
    source_station_id bigint not null,
    target_station_id bigint not null,
    primary key(id)
);
