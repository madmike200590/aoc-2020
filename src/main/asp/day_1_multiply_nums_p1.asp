adding_up(N1, N2) :- num(N1), num(N2), N1 + N2 = 2020.
result(R) :- adding_up(N1, N2), R = N1 * N2.
