adding_up(N1, N2, N3) :- num(N1), num(N2), num(N3), N1 + N2 = INTM, N3 + INTM = 2020.
result(R) :- adding_up(N1, N2, N3), INTM = N1 * N2, R = INTM * N3.
