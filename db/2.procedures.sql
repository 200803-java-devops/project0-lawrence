
create view get_leaderboard_data as 
    select username, score from user_entry join leaderboard_entry on (user_entry.id = leaderboard_entry.user_id) 
    order by score desc 
    limit 10;


