drop database if exists hospital;
create database hospital; 
use hospital;

create table `user` (
	id int(11) not null auto_increment,
    userType varchar (50) not null,
	firstName varchar(50) not null,
    lastName varchar(50) not null,
    patronymic varchar(50) not null,
    login varchar(50) not null,
    `password` varchar(50) not null,
	primary key (id)
) engine=INNODB default charset=utf8;

create table admin (
	id int(11) not null auto_increment,
    userid int(11) not null,
    `position` varchar(50) not null,
    primary key (id),
    foreign key (userid) references `user` (id) ON DELETE CASCADE
) engine=INNODB default charset=utf8;

create table speciality (
	id int (11) not null auto_increment,
    speciality varchar(50) not null,
    primary key (id)
) engine=INNODB default charset=utf8;

create table room (
	id int (11) not null auto_increment,
    room varchar(50) not null,
    primary key (id)
) engine=INNODB default charset=utf8;

create table doctor (
	id int(11) not null auto_increment,
    userid int(11) not null,
    specialityid int(11) not null,
    roomid int(11) not null,
    datestart date not null,
    dateend date not null,
    duration int(11) not null,
    primary key (id),
    foreign key (userid) references `user` (id) ON DELETE CASCADE,
    foreign key (specialityid) references speciality (id),
    foreign key (roomid) references room (id)
) engine=INNODB default charset=utf8;

create table patient (
	id int (11) not null auto_increment,
    userid int(11) not null,
    email varchar(50) not null,
    address varchar(50) not null,
    phone varchar(50) not null,
    primary key (id),
    foreign key (userid) references `user` (id) ON DELETE CASCADE
) engine=INNODB default charset=utf8;

create table `daySchedule` (
    id int (11) not null auto_increment,
    doctorid int(11) not null,
    dateDay date not null,
    primary key (id),
    foreign key (doctorid) references doctor (id) on delete cascade
) engine=INNODB default charset=utf8;

create table appointment (
	id int(11) not null auto_increment,
    dayscheduleid int(11) not null,
    patientid int(11) default null,
    ticket varchar(50) default null,
    `time` time not null,
    primary key(id),
    foreign key (dayscheduleid) references `daySchedule` (id) on delete cascade,
    foreign key (patientid) references patient (id) on delete cascade
) engine=InnoDB default charset=utf8;

create table `session` (
	id int(11) not null auto_increment,
	personid int(11) not null,
    cookie varchar(50) not null,
    primary key(id)
) engine=InnoDB default charset=utf8;


 
 

