#enumeration_predicate_is enum.

% Config
passwd_delimiter(":").

% Get passwd specs from here
infile("/home/michael/advent_of_code/day_2_p1_input.txt").

% Read lines from file (decouple so we don't have file io during grounding in the rules doing the actual work)
input_line(LINE) :- infile(PATH), &file_line[PATH](LINE).

input_line_arr(LINE_ARR) :- 
    input_line(LINE),
    &string_split[LINE, DELIM](LINE_ARR), 
    passwd_delimiter(DELIM).

% Processes input lines of form input_line_arr(["n-k C", "passwd"])
policy_passwd(policy(CHARSEQ, MIN_OCCURS, MAX_OCCURS), PASSWD) :- 
    input_line_arr(LINE_ARR),
    &array_idx_value[LINE_ARR, 1](PASSWD_RAW),
    &array_idx_value[LINE_ARR, 0](POLICY_STR),
    &string_trim[PASSWD_RAW](PASSWD),
    &string_split[POLICY_STR, " "](POLICY_ARR),
    &array_idx_value[POLICY_ARR, 1](CHARSEQ),
    &array_idx_value[POLICY_ARR, 0](OCCURS_STR),
    &string_split[OCCURS_STR, "-"](OCCURS_ARR),
    &array_idx_value[OCCURS_ARR, 0](MIN_OCCURS_STR),
    &array_idx_value[OCCURS_ARR, 1](MAX_OCCURS_STR),
    &string_parse_integer[MIN_OCCURS_STR](MIN_OCCURS),
    &string_parse_integer[MAX_OCCURS_STR](MAX_OCCURS).

% Check policy
satisfies_policy_part1(PASSWD, policy(CHARSEQ, MIN_OCCURS, MAX_OCCURS)) :- 
    policy_passwd(policy(CHARSEQ, MIN_OCCURS, MAX_OCCURS), PASSWD),
    &string_substring_occurences[PASSWD, CHARSEQ](ACT_OCCURS),
    ACT_OCCURS >= MIN_OCCURS,
    ACT_OCCURS <= MAX_OCCURS.

satisfies_policy_part2_pos1(PASSWD, policy(REFCHAR, EITHER_AT, OR_AT)) :- 
    policy_passwd(policy(REFCHAR, EITHER_AT, OR_AT), PASSWD),
    CHECK_IDX = EITHER_AT - 1, % Toboggan Corporate Policies have no concept of "index zero"
    &string_char_at[PASSWD, CHECK_IDX](CHECK_CHAR),
    CHECK_CHAR = REFCHAR.

satisfies_policy_part2_pos2(PASSWD, policy(REFCHAR, EITHER_AT, OR_AT)) :- 
    policy_passwd(policy(REFCHAR, EITHER_AT, OR_AT), PASSWD),
    CHECK_IDX = OR_AT - 1, % Toboggan Corporate Policies have no concept of "index zero"
    &string_char_at[PASSWD, CHECK_IDX](CHECK_CHAR),
    CHECK_CHAR = REFCHAR.

satisfies_policy(PASSWD, POLICY) :-
    satisfies_policy_part2_pos1(PASSWD, POLICY),
    not satisfies_policy_part2_pos2(PASSWD, POLICY).

satisfies_policy(PASSWD, POLICY) :-
    satisfies_policy_part2_pos2(PASSWD, POLICY),
    not satisfies_policy_part2_pos1(PASSWD, POLICY).

% For debugging
violates_policy(PASSWD, POLICY) :-
    policy_passwd(POLICY, PASSWD),
    not satisfies_policy(PASSWD, POLICY).

% Alpha has MASSIVE performance issues with count aggregate evaluation...
% valid_passwords(CNT) :- CNT = #count{X : satisfies_policy(X)}.
    
% ... enum-based hack instead: since this program is stratified,
% we can enumerate and use the max index for count
% add one, enum index starts at 0
valid_value_idx(I) :- satisfies_policy(PASSWD, _), enum(enum, PASSWD, I). 
valid_passwords(CNT) :- CNT = #max{I : valid_value_idx(I)}. 