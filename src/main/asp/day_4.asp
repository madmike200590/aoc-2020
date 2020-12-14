#enumeration_predicate_is enum.

infile("/home/michael/advent_of_code/day_4_input.txt").
data_item_regex("((byr)|(iyr)|(eyr)|(hgt)|(hcl)|(ecl)|(pid)|(cid)):[#a-zA-Z0-9]+").

data_field_regex(byr, "byr:[#a-zA-Z0-9]+").
data_field_regex(iyr, "iyr:[#a-zA-Z0-9]+").
data_field_regex(eyr, "eyr:[#a-zA-Z0-9]+").
data_field_regex(hgt, "hgt:[#a-zA-Z0-9]+").
data_field_regex(hcl, "hcl:[#a-zA-Z0-9]+").
data_field_regex(ecl, "ecl:[#a-zA-Z0-9]+").
data_field_regex(pid, "pid:[#a-zA-Z0-9]+").
data_field_regex(cid, "cid:[#a-zA-Z0-9]+").

data_field_validate_regex(byr, "byr:((19[2-9][0-9])|(200[0-2]))").
data_field_validate_regex(iyr, "iyr:((201[0-9])|(2020))").
data_field_validate_regex(eyr, "eyr:((202[0-9])|(2030))").
data_field_validate_regex(hgt, "hgt:(((([5-6][0-9])|(7[0-6]))in)|(1(([5-8][0-9])|(9[0-3])))cm)").
data_field_validate_regex(hcl, "hcl:#[0-9a-f]{6}").
data_field_validate_regex(ecl, "ecl:((amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth))").
data_field_validate_regex(pid, "pid:[0-9]{9}").
data_field_validate_regex(cid, "cid:[#a-zA-Z0-9]+").

input_line(IDX, LINE) :-
    infile(PATH),
    &file_line_no[PATH](LINE, IDX_STR),
    &string_parse_integer[IDX_STR](IDX).

% Everything between two consecutive lines is one item that needs to be parsed
% Find pairs of consecutive empty lines
empty_line(IDX) :- input_line(IDX, LINE), &string_is_empty[LINE].
empty_line_after(IDX, IDX_AFTER) :- empty_line(IDX), empty_line(IDX_AFTER), IDX_AFTER > IDX.
empty_lines_not_consecutive(IDX, IDX_AFTER) :- empty_line_after(IDX, IDX_INTM), empty_line_after(IDX_INTM, IDX_AFTER).
parse_area(IDX_START, IDX_END) :- 
    empty_line(IDX_START),
    empty_line(IDX_END),
    empty_line_after(IDX_START, IDX_END),
    not empty_lines_not_consecutive(IDX_START, IDX_END).

% The first item we need to parse has no empty line in front of it
parse_area(-1, MIN_EMPTY) :- MIN_EMPTY = #min{L : empty_line(L)}.
% Likewise, the last item has no empty line after
num_lines(LINES) :- MAX_IDX = #max{IDX : input_line(IDX, _)}, LINES = MAX_IDX + 1.
parse_area(MAX_EMPTY, LINES) :- num_lines(LINES), MAX_EMPTY = #max{L : empty_line(L)}.

%%% Actual Parsing
parse_area_item(area(AREA_START, AREA_END), ITEM) :- 
    input_line(IDX, ITEM), IDX > AREA_START, IDX < AREA_END,
    parse_area(AREA_START, AREA_END).

parse_area_item_parseable(AREA, ITEM) :- 
    parse_area_item(AREA, ITEM), 
    &stdlib_string_matches_regex[ITEM, REGEX],
    data_item_regex(REGEX).

parse_area_parts(AREA, PART_ARR) :-
    parse_area_item(AREA, ITEM),
    not parse_area_item_parseable(AREA, ITEM),
    &string_split[ITEM, " "](PART_ARR).

% use a name other than parse_area_item here so we stay stratified (performance)
parse_area_parse_input(AREA, INPUT) :-
    parse_area_item_parseable(AREA, INPUT).

parse_area_parse_input(AREA, INPUT) :-
    parse_area_parts(AREA, PARTS),
    &lst_idx_value[PARTS, IDX](INPUT),
    &lst_length[PARTS](PARTS_LEN),
    IDX = 0..MAX_IDX,
    MAX_IDX = PARTS_LEN - 1.

passport_data_field(AREA, ID, VALUE) :-
    parse_area_parse_input(AREA, VALUE),
    &stdlib_string_matches_regex[VALUE, FIELD_REGEX],
    data_field_regex(ID, FIELD_REGEX).

passport_data_field_valid(AREA, ID, VALUE) :-
    passport_data_field(AREA, ID, VALUE),
    &stdlib_string_matches_regex[VALUE, VALIDATE_REGEX],
    data_field_validate_regex(ID, VALIDATE_REGEX).

% Derive this explicitly for debugging
passport_data_field_invalid(AREA, ID, VALUE) :-
    passport_data_field(AREA, ID, VALUE),
    not passport_data_field_valid(AREA, ID, VALUE).

passport_valid(area(S, E)) :-
    parse_area(S, E), 
    passport_data_field_valid(area(S, E), byr, _),
    passport_data_field_valid(area(S, E), iyr, _),
    passport_data_field_valid(area(S, E), eyr, _),
    passport_data_field_valid(area(S, E), hgt, _),
    passport_data_field_valid(area(S, E), hcl, _),
    passport_data_field_valid(area(S, E), ecl, _),
    passport_data_field_valid(area(S, E), pid, _).

% % Once more, use an enum as crutch for a count aggregate
% valid_idx(I) :- passport_valid(A), enum(valids, A, I).
% result(CNT) :- CNT = #max{I : valid_idx(I)}.

result(CNT) :- CNT = #count{ID : passport_valid(ID)}.
