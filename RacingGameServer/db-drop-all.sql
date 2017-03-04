alter table car drop constraint if exists fk_car_race_id;
drop index if exists ix_car_race_id;

alter table lap drop constraint if exists fk_lap_race;

alter table track_segment drop constraint if exists fk_track_segment_lap_id;
drop index if exists ix_track_segment_lap_id;

alter table turn drop constraint if exists fk_turn_car_id;
drop index if exists ix_turn_car_id;

drop table if exists car;

drop table if exists lap;

drop table if exists race;

drop table if exists track_segment;

drop table if exists turn;

