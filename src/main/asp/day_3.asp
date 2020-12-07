#enumeration_predicate_is enum.

infile("/home/michael/advent_of_code/day_3_p1_input.txt").
start_pos(0,0).
run_travel_gradient(1, 1, 1). % (RIGHT, DOWN)
run_travel_gradient(2, 3, 1). % (RIGHT, DOWN)
run_travel_gradient(3, 5, 1). % (RIGHT, DOWN)
run_travel_gradient(4, 7, 1). % (RIGHT, DOWN)
run_travel_gradient(5, 1, 2). % (RIGHT, DOWN)

map_line(IDX, CONTENT) :- 
    infile(PATH),
    &file_line_no[PATH](CONTENT, IDX_STR),
    &string_parse_integer[IDX_STR](IDX).

map_height(H) :- MAX_IDX = #max{IDX : map_line(IDX, _)}, H = MAX_IDX + 1.
map_base_width(W) :- &stdlib_string_length[LINE](W), map_line(_, LINE).

% Translate raw map data to object information
interesting_x(0..MAX) :- map_base_width(W), MAX = W - 1.
interesting_y(0..MAX) :- map_height(H), MAX = H - 1.

object_at(open, pos(X, Y)) :-
    interesting_x(X),
    interesting_y(Y),
    map_line(Y, LINE),
    &string_char_at[LINE, X](CHAR),
    CHAR = ".".

object_at(tree, pos(X, Y)) :-
    interesting_x(X),
    interesting_y(Y),
    map_line(Y, LINE),
    &string_char_at[LINE, X](CHAR),
    CHAR = "#".

% % calc needed width
% required_width(REQ_WIDTH) :- 
%     travel_gradient(RIGHT_UNIT, DOWN_UNIT), 
%     map_height(HEIGHT),
%     STEPS_DOWN = HEIGHT / DOWN_UNIT, 
%     REQ_WIDTH = STEPS_DOWN * RIGHT_UNIT.

% % Extrapolate objects to the right
% object_at(OBJ, pos(X, Y)) :-
%     object_at(OBJ, pos(X_LEFT, Y)),
%     map_base_width(BASE_WIDTH),
%     required_width(REQ_WIDTH),
%     X = X_LEFT + BASE_WIDTH,
%     X < REQ_WIDTH + BASE_WIDTH,
%     X > BASE_WIDTH.

% Check passed coordinates
run_visited_pos(R, X, Y) :- start_pos(X, Y), run_travel_gradient(R, _, _).
run_visited_pos(R, X, Y) :- 
    run_visited_pos(R, LAST_X, LAST_Y), 
    run_travel_gradient(R, RIGHT, DOWN),
    map_height(HEIGHT),
    map_base_width(BASE_WIDTH),
    X_RAW = LAST_X + RIGHT,
    X = X_RAW \ BASE_WIDTH, % modulo
    Y = LAST_Y + DOWN,
    Y <= HEIGHT.

% Collect hit trees
run_hit_tree_at(R, X, Y) :-
    run_visited_pos(R, X, Y),
    object_at(tree, pos(X, Y)).

%cnt_trees(TREES) :- TREES = #count{X, Y : hit_tree_at(X, Y)}.
% Once again, we need an enum hack to account for performance issues with count aggregates
run_tree_idx(R, I) :- run_hit_tree_at(R, X, Y), enum(trees(R), tpl(X, Y), I).

% For whatever reason, Alpha seems to have a problem with the global variable, TODO debug properly!
% Workaround: use compiled aggregate directly
%cnt_trees(R, CNT) :- CNT = #max{I : run_tree_idx(R, I)}, run_travel_gradient(R, _, _).
cnt_trees(R, CNT) :- max_2_result(max_2_args(R), CNT), run_travel_gradient(R, _, _).
max_2_result(ARGS, M) :- max_2_max_element_tuple(ARGS, M).
max_2_max_element_tuple(ARGS, MAX) :- max_2_element_tuple(ARGS, MAX), not max_2_element_tuple_has_greater(ARGS, MAX).
max_2_element_tuple(max_2_args(R), I) :- run_tree_idx(R, I), run_travel_gradient(R, _, _).
max_2_element_tuple_less_than(ARGS, LESS, THAN) :- max_2_element_tuple(ARGS, LESS), max_2_element_tuple(ARGS, THAN), LESS < THAN.
max_2_element_tuple_has_greater(ARGS, TPL) :- max_2_element_tuple_less_than(ARGS, TPL, _).