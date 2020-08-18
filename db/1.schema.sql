create table user_entry (
    id serial primary key,
    username text unique not null
);

insert into user_entry (username) values ('test');

create table token (
    token_string text not null primary key,
    holder int references user_entry(id)
);

insert into token (token_string, holder) values ('test', 1);

create table leaderboard_entry (
    user_id integer primary key references user_entry(id),
    score integer
);