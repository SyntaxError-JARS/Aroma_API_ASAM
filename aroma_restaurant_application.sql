
-- Creating schema for Aroma Restaurant Application
create schema aroma_restaurant_application;

--Creating customer table 
create table customer (
   username varchar (25) primary key,
   fname varchar (20) not null,
   lname varchar (20) not null,
   "password" varchar (32) not null,
   balance numeric not null,
   is_Admin boolean default false
  
);

--Creating Credit Card table

create table credit_card (
 cc_number varchar (16) primary key,
 cc_name varchar (20) not null,
 cvv int not null,
 exp_date varchar (15) not null,
 zip int not null,
 "limit" numeric not null,
 customer_username varchar (25)

)


--Creating a foreign key relationship between customer table and credit card table 

alter table credit_card 
add constraint  fk_username_cc 
foreign key(customer_username) references customer(username)

--Creating a foreign key relationship between customer table and order table 
alter table "order"
add constraint fk_username_or
foreign key (customer_username) references customer (username);

--Creating order table 

create table "order" (
 id int primary key,
 menu_item varchar (50) not null,
 "comment" text,
 isFavorite boolean default false,
 order_date varchar (15) not null,
 customer_username varchar (25)

)

--Creating a Menu table 

create table menu (
  item_name varchar (50) primary key,
  "cost" numeric not null,
  protein varchar (20),
  is_substituable boolean default false

);

--Creating a foreign key relationship between menu table and order table 
alter table "order"
add constraint fk_item_number
foreign key (menu_item) references menu (item_name);


-- Inserting data to customer table
insert into customer 
values ('asebirka','Ay', 'Sebirka', 'apassword',1000.00, 'true');

insert into customer 
values ('aminase','Asrat', 'Minase', 'apassword',1000.00, 'true');

insert into customer 
values ('ajester','Charles', 'Jester', 'apassword',1000.00, 'false');

-- Inserting data to credit_card table

insert into credit_card  
values ('2300000001011200','Ay_Sebirka ', 369, '05/24',78245, 2000.00, 'asebirka' );

insert into credit_card  
values ('2200000001011200','Asrat_Minase ', 111, '06/23',78660, 13000.00, 'aminase' );

insert into credit_card  
values ('5600000001011200','Charles_Jester ', 785, '08/22',25463, 40000.00, 'ajester' );


-- Inserting data to order table

insert into "order"
values (1,'Hamburger', 'Jalapeno Cheddar Burgers! These are amazing with turkey or beef and can easily be broiled.', 'false',  '02-11-2022', 'asebirka' );

insert into "order" 
values (2,'Doro-wot', 'A central ingredient of Doro Wat is Berbere, a fiery, bright red and flavorful Ethiopian spice blend.','true', '01-01-2022', 'aminase');

insert into "order" 
values (3,'Curly_Fries', 'Curly Fries are the perfect blend of crispy fried potatoes and mouthwatering seasoning.','false', '01-11-2022', 'ajester' );


-- Inserting data to menu table

insert into menu  
values ('Hamburger',12.52, 'medium', 'true');

insert into menu  
values ('Doro-wot',15.55, 'medium', 'true');

insert into menu  
values ('Curly_Fries',10.00, 'low', 'true');





