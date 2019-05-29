/**
 * CREATE Script for init of DB
 */

-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (1, now(), false, 'OFFLINE',
'driver01pw', 'driver01');

insert into driver (id, date_created, deleted, online_status, password, username) values (2, now(), false, 'OFFLINE',
'driver02pw', 'driver02');

insert into driver (id, date_created, deleted, online_status, password, username) values (3, now(), false, 'OFFLINE',
'driver03pw', 'driver03');


-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (4, now(), false, 'ONLINE',
'driver04pw', 'driver04');

insert into driver (id, date_created, deleted, online_status, password, username) values (5, now(), false, 'ONLINE',
'driver05pw', 'driver05');

insert into driver (id, date_created, deleted, online_status, password, username) values (6, now(), false, 'ONLINE',
'driver06pw', 'driver06');

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'driver07pw', 'driver07');

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'driver08pw', 'driver08');

-- Create 3 Manufacturer
insert into manufacturer (name) values ('BMW');

insert into manufacturer (name) values ('Mercedes Benz');

insert into manufacturer (name) values ('Audi');

-- Create 3 car
insert into car (id, convertible, deleted, engine_type, license_plate, manufacturer_name, number_of_ratings, rating, seat_count)
 values (1, false, false, 'DIESEL', '123LY', 'BMW', 5, 3, 4);

insert into car (id, convertible, deleted, engine_type, license_plate, manufacturer_name, number_of_ratings, rating, seat_count)
 values (2, false, false, 'ELECTRIC', '456DT', 'Mercedes Benz', 15, 3.5, 4);

insert into car (id, convertible, deleted, engine_type, license_plate, manufacturer_name, number_of_ratings, rating, seat_count)
 values (3, true, false, 'GAS', '789DU', 'Audi', 45, 3.53, 2);

-- Update 1 ONLINE driver with associated car
update driver set
            car_id=1,
            online_status='ONLINE',
            password='driver04pw',
            username='driver04'
        where
            id=4;
