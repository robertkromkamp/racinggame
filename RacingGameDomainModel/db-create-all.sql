create table car (
  id                            bigint auto_increment not null,
  finished_timestamp            timestamp,
  color                         integer,
  current_speed                 integer,
  current_direction             integer,
  current_offset                integer,
  race_id                       bigint,
  driver_name                   varchar(255),
  nr_segments_travelled         integer,
  laps_completed                integer,
  constraint ck_car_color check ( color in (0,1,2,3,4,5,6,7,8)),
  constraint ck_car_current_speed check ( current_speed in (0,1,2,3)),
  constraint ck_car_current_direction check ( current_direction in (0,1,2,3)),
  constraint ck_car_current_offset check ( current_offset in (0,1,2,3,4)),
  constraint pk_car primary key (id)
);

create table lap (
  id                            bigint auto_increment not null,
  race                          bigint,
  circuit_name                  varchar(255),
  constraint uq_lap_race unique (race),
  constraint pk_lap primary key (id)
);

create table race (
  id                            bigint auto_increment not null,
  nr_of_laps                    integer,
  started_timestamp             timestamp,
  ended_timestamp               timestamp,
  constraint pk_race primary key (id)
);

create table track_segment (
  id                            bigint auto_increment not null,
  lap_id                        bigint,
  direction                     integer,
  constraint ck_track_segment_direction check ( direction in (0,1,2,3)),
  constraint pk_track_segment primary key (id)
);

create table turn (
  id                            bigint auto_increment not null,
  direction                     integer,
  speed                         integer,
  car_id                        bigint,
  constraint ck_turn_direction check ( direction in (0,1,2,3)),
  constraint ck_turn_speed check ( speed in (0,1,2,3)),
  constraint pk_turn primary key (id)
);

alter table car add constraint fk_car_race_id foreign key (race_id) references race (id) on delete restrict on update restrict;
create index ix_car_race_id on car (race_id);

alter table lap add constraint fk_lap_race foreign key (race) references race (id) on delete restrict on update restrict;

alter table track_segment add constraint fk_track_segment_lap_id foreign key (lap_id) references lap (id) on delete restrict on update restrict;
create index ix_track_segment_lap_id on track_segment (lap_id);

alter table turn add constraint fk_turn_car_id foreign key (car_id) references car (id) on delete restrict on update restrict;
create index ix_turn_car_id on turn (car_id);

