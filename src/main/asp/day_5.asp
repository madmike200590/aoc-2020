%%%% Puzzle Description (Part 1, see https://adventofcode.com/2020/day/5) %%%%
%
% --- Day 5: Binary Boarding ---
% You board your plane only to discover a new problem: you dropped your boarding pass! 
% You aren't sure which seat is yours, and all of the flight attendants are busy with the 
% flood of people that suddenly made it through passport control.

% You write a quick program to use your phone's camera to scan all of the nearby boarding 
% passes (your puzzle input); perhaps you can find your seat through process of elimination.

% Instead of zones or groups, this airline uses binary space partitioning to seat people. 
% A seat might be specified like FBFBBFFRLR, where F means "front", B means "back", L means "left", and R means "right".

% The first 7 characters will either be F or B; 
% these specify exactly one of the 128 rows on the plane (numbered 0 through 127). 
% Each letter tells you which half of a region the given seat is in. Start with the whole list of rows; 
% the first letter indicates whether the seat is in the front (0 through 63) or the back (64 through 127). 
% The next letter indicates which half of that region the seat is in, and so on until you're left with exactly one row.

% For example, consider just the first seven characters of FBFBBFFRLR:

% Start by considering the whole range, rows 0 through 127.
% F means to take the lower half, keeping rows 0 through 63.
% B means to take the upper half, keeping rows 32 through 63.
% F means to take the lower half, keeping rows 32 through 47.
% B means to take the upper half, keeping rows 40 through 47.
% B keeps rows 44 through 47.
% F keeps rows 44 through 45.
% The final F keeps the lower of the two, row 44.
% The last three characters will be either L or R; 
% these specify exactly one of the 8 columns of seats on the plane (numbered 0 through 7). 
% The same process as above proceeds again, this time with only three steps. 
% L means to keep the lower half, while R means to keep the upper half.

% For example, consider just the last 3 characters of FBFBBFFRLR:

% Start by considering the whole range, columns 0 through 7.
% R means to take the upper half, keeping columns 4 through 7.
% L means to take the lower half, keeping columns 4 through 5.
% The final R keeps the upper of the two, column 5.
% So, decoding FBFBBFFRLR reveals that it is the seat at row 44, column 5.

% Every seat also has a unique seat ID: multiply the row by 8, then add the column. 
% In this example, the seat has ID 44 * 8 + 5 = 357.

% Here are some other boarding passes:

% BFFFBBFRRR: row 70, column 7, seat ID 567.
% FFFBBBFRRR: row 14, column 7, seat ID 119.
% BBFFBBFRLL: row 102, column 4, seat ID 820.
% As a sanity check, look through your list of boarding passes. What is the highest seat ID on a boarding pass?

infile("/home/michael/advent_of_code/inputs/day5.txt").

encoding_scheme("B", "F", "[BF]+").
encoding_scheme("R", "L", "[RL]+").

% Get file content as individual lines (i.e. 10-letter strings encoding a seat id)
input_line(LINE) :- infile(PATH), &file_line[PATH](LINE).

% First, split an input line into row and column parts
seat_spec(ROWSTR, COLSTR) :- 
    input_line(LINE),
    &string_substring[LINE, 0, 7](ROWSTR),
    &string_substring[LINE, 7, 10](COLSTR).

% Above "partitioning scheme" is really just a fancy way of saying "those are binary strings".
% So "B" means the bit is set (i.e. 1), "F" means the bit in question is zero

% The row string..
binstring_decoded(BINSTR, 0) :- seat_spec(BINSTR, _).
% .. as well as the column string are binary numbers we want to decode
binstring_decoded(BINSTR, 0) :- seat_spec(_ , BINSTR).

% Helper - binstring_intm_decoded is the "internal" predicate we're using to recursively add up the bit values
binstring_intm_decoded(START_STR, START_STR, 0) :- binstring_decoded(START_STR, 0). 

% Handle the case where the bit is set
binstring_intm_decoded(START_STR, CURR_STR, CURR_VALUE) :- 
    binstring_intm_decoded(START_STR, LAST_STR, LAST_VALUE),
    &str_x_xs[LAST_STR](HIGH_CODE, CURR_STR),
    &stdlib_string_length[LAST_STR](LAST_LEN),
    CURR_VALUE = LAST_VALUE + 2 ** (LAST_LEN - 1),
    encoding_scheme(HIGH_CODE, _, REGEX),
    &stdlib_string_matches_regex[START_STR, REGEX].

% Handle the case where the bit is not set
binstring_intm_decoded(START_STR, CURR_STR, CURR_VALUE) :- 
    binstring_intm_decoded(START_STR, LAST_STR, LAST_VALUE),
    &str_x_xs[LAST_STR](LOW_CODE, CURR_STR),
    &stdlib_string_length[LAST_STR](LAST_LEN),
    CURR_VALUE = LAST_VALUE,
    encoding_scheme(_, LOW_CODE, REGEX),
    &stdlib_string_matches_regex[START_STR, REGEX].

% Fiddle the decoded values back into seat specs
decoded_seat_spec(ROW, COL, SEAT_ID, ROWSTR, COLSTR) :-
    seat_spec(ROWSTR, COLSTR),
    binstring_intm_decoded(ROWSTR, "", ROW),
    binstring_intm_decoded(COLSTR, "", COL),
    SEAT_ID = 8 * ROW + COL.
    
% Finally, get the puzzle result for part 1 (i.e. the max seat id)
result(part1, R) :- R = #max{ID : decoded_seat_spec(_, _, ID, _, _)}.

%%%% Part 2 %%%%

% --- Part Two ---
% Ding! The "fasten seat belt" signs have turned on. Time to find your seat.

% It's a completely full flight, so your seat should be the only missing boarding pass in your list. 
% However, there's a catch: some of the seats at the very front and back of the plane don't exist on this aircraft, 
% so they'll be missing from your list as well.

% Your seat wasn't at the very front or back, though; the seats with IDs +1 and -1 from yours will be in your list.

% What is the ID of your seat?

%% Find a "gap" of exactly one ID in the sequence of seat IDs to get the solution
seat_id(ID) :- decoded_seat_spec(_, _, ID, _, _).
result(part2, MIDDLE_ID) :- 
    seat_id(LOWER_ID), 
    seat_id(UPPER_ID), 
    not seat_id(MIDDLE_ID),
    MIDDLE_ID = LOWER_ID + 1,
    UPPER_ID = LOWER_ID + 2.