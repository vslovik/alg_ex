/**
 * Programming Assignment 3: Baseball Elimination
 * <p/>
 * Given the standings in a sports division at some point during the season,
 * determine which teams have been mathematically eliminated from winning
 * their division.
 * <p/>
 * In the baseball elimination problem, there is a division consisting of N teams.
 * At some point during the season, team i has
 * w[i] wins,
 * l[i] losses,
 * r[i] remaining games,
 * and g[i][j] games left to play against team j.
 * <p/>
 * A team is mathematically eliminated if it cannot possibly finish the season in (or tied for) first place.
 * The goal is to determine exactly which teams are mathematically eliminated.
 * <p/>
 * For simplicity, we assume that no games end in a tie (as is the case in Major League Baseball)
 * and that there are no rainouts (i.e., every scheduled game is played).
 * <p/>
 * The problem is not as easy as many sports writers would have you believe,
 * in part because the answer depends not only on the number of games won and left to play,
 * but also on the schedule of remaining games.
 * To see the complication, consider the following scenario:
 * <p/>
 * w[i] l[i] r[i]        g[i][j]
 * i  team         wins loss left   Atl Phi NY  Mon
 * ------------------------------------------------
 * 0  Atlanta       83   71    8     -   1   6   1
 * 1  Philadelphia  80   79    3     1   -   0   2
 * 2  New York      78   78    6     6   0   -   0
 * 3  Montreal      77   82    3     1   2   0   -
 * <p/>
 * Montreal is mathematically eliminated since it can finish with at most 80 wins and Atlanta already has 83 wins.
 * This is the simplest reason for elimination.
 * <p/>
 * However, there can be more complicated reasons. For example, Philadelphia is also mathematically eliminated.
 * It can finish the season with as many as 83 wins, which appears to be enough to tie Atlanta.
 * But this would require Atlanta to lose all of its remaining games, including the 6 against New York,
 * in which case New York would finish with 84 wins.
 * We note that New York is not yet mathematically eliminated despite the fact that it has fewer wins than Philadelphia.
 * <p/>
 * It is sometimes not so easy for a sports writer to explain why a particular team is mathematically eliminated.
 * Consider the following scenario from the American League East on August 30, 1996:
 * w[i] l[i] r[i]          g[i][j]
 * i  team         wins loss left   NY Bal Bos Tor Det
 * ---------------------------------------------------
 * 0  New York      75   59   28     -   3   8   7   3
 * 1  Baltimore     71   63   28     3   -   2   7   4
 * 2  Boston        69   66   27     8   2   -   0   0
 * 3  Toronto       63   72   27     7   7   0   -   0
 * 4  Detroit       49   86   27     3   4   0   0   -
 * <p/>
 * It might appear that Detroit has a remote chance of catching New York and winning the division because Detroit
 * can finish with as many as 76 wins if they go on a 27-game winning steak, which is one more than New York would have
 * if they go on a 28-game losing streak. Try to convince yourself that Detroit is already mathematically eliminated.
 * Here's one ad hoc explanation; we will present a simpler explanation below.
 * <p/>
 * A maxflow formulation.   We now solve the baseball elimination problem by reducing it to the maxflow problem.
 * To check whether team x is eliminated, we consider two cases.
 * <p/>
 * Trivial elimination. If the maximum number of games team x can win is less than the number of wins of some other team i,
 * then team x is trivially eliminated (as is Montreal in the example above). That is, if w[x] + r[x] < w[i],
 * then team x is mathematically eliminated.
 * <p/>
 * Nontrivial elimination. Otherwise, we create a flow network and solve a maxflow problem in it. In the network,
 * feasible integral flows correspond to outcomes of the remaining schedule.
 * <p/>
 * There are vertices corresponding to teams (other than team x) and to remaining divisional games (not involving team x).
 * Intuitively, each unit of flow in the network corresponds to a remaining game.
 * As it flows through the network from s to t, it passes from
 * a game vertex, say between teams i and j,
 * then through one of the team vertices i or j,
 * classifying this game as being won by that team
 * <p/>
 * More precisely, the flow network includes the following edges and capacities.
 * <p/>
 * - We connect an artificial source vertex s to each game vertex i-j and set its capacity to g[i][j].
 * If a flow uses all g[i][j] units of capacity on this edge,
 * then we interpret this as playing all of these games, with the wins distributed between the team vertices i and j.
 * <p/>
 * - We connect each game vertex i-j with the two opposing team vertices to ensure that one of the two teams earns a win.
 * We do not need to restrict the amount of flow on such edges.
 * <p/>
 * - Finally, we connect each team vertex to an artificial sink vertex t. We want to know if there is some way of
 * completing all the games so that team x ends up winning at least as many games as team i. Since team x can win as
 * many as w[x] + r[x] games, we prevent team i from winning more than that many games in total, by including an edge
 * from team vertex i to the sink vertex with capacity w[x] + r[x] - w[i].
 * <p/>
 * If all edges in the maxflow that are pointing from s are full, then this corresponds to assigning winners to all of
 * the remaining games in such a way that no team wins more games than x. If some edges pointing from s are not full,
 * then there is no scenario in which team x can win the division.
 * <p/>
 * What the min cut tells us.   By solving a maxflow problem, we can determine whether a given team is mathematically
 * eliminated. We would also like to explain the reason for the team's elimination to a friend in nontechnical terms
 * (using only grade-school arithmetic). Here's such an explanation for Detroit's elimination in the American League
 * East example above. With the best possible luck, Detroit finishes the season with 49 + 27 = 76 wins. Consider the
 * subset of teams R = { New York, Baltimore, Boston, Toronto }. Collectively, they already have 75 + 71 + 69 + 63 = 278
 * wins; there are also 3 + 8 + 7 + 2 + 7 = 27 remaining games among them, so these four teams must win at least an
 * additional 27 games. Thus, on average, the teams in R win at least 305 / 4 = 76.25 games. Regardless of the outcome,
 * one team in R will win at least 77 games, thereby eliminating Detroit.
 * <p/>
 * In fact, when a team is mathematically eliminated there always exists such a convincing certificate of elimination,
 * where R is some subset of the other teams in the division. Moreover, you can always find such a subset R by choosing
 * the team vertices on the source side of a min s-t cut in the baseball elimination network. Note that although we
 * solved a maxflow/mincut problem to find the subset R, once we have it, the argument for a team's elimination
 * involves only grade-school algebra.
 * <p/>
 * Your assignment.   Write an immutable data type BaseballElimination that represents a sports division and determines
 * which teams are mathematically eliminated by implementing the following API:
 * <p/>
 * Input format.   The input format is the number of teams in the division N followed by one line for each team. Each
 * line contains the team name (with no internal whitespace characters), the number of wins, the number of losses,
 * the number of remaining games, and the number of remaining games against each team in the divsion. For example,
 * the input files teams4.txt and teams5.txt correspond to the two examples discussed above.
 * <p/>
 * % more teams4.txt
 * 4
 * Atlanta       83 71  8  0 1 6 1
 * Philadelphia  80 79  3  1 0 0 2
 * New_York      78 78  6  6 0 0 0
 * Montreal      77 82  3  1 2 0 0
 * <p/>
 * % more teams5.txt
 * 5
 * New_York    75 59 28   0 3 8 7 3
 * Baltimore   71 63 28   3 0 2 7 4
 * Boston      69 66 27   8 2 0 0 0
 * Toronto     63 72 27   7 7 0 0 0
 * Detroit     49 86 27   3 4 0 0 0
 * <p/>
 * You may assume that N ≥ 1 and that the input files are in the specified format and internally consistent.
 * Note that a team's number of remaining games does not necessarily equal the sum of the remaining games against teams
 * in its division because a team may play opponents outside its division.
 * <p/>
 * How do I compute the mincut? The inCut() method in FordFulkerson.java takes a vertex as an argument and returns true
 * if that vertex is on the s-side of the mincut.
 * <p/>
 * How do I specify an infinite capacity for an edge? Use Double.POSITIVE_INFINITY.
 * <p/>
 * To verify that you are returning a valid certificate of elimination R, compute a(R) = (w(R) + g(R)) / |R|, where w(R)
 * is the total number of wins of teams in R, g(R) is the total number of remaining games between teams in R, and |R|
 * is the number of teams in R. Check that a(R) is greater than the maximum number of games the eliminated team can win
 * <p/>
 * These are purely suggestions for how you might make progress. You do not have to follow these steps.
 * <p/>
 * - Write code to read in the input file and store the data.
 * <p/>
 * - Draw by hand the FlowNetwork graph that you want to construct for a few small examples. Write the code to construct
 * the FlowNetwork, print it out using the toString() method, and and make sure that it matches your intent. Do not continue
 * until you have thoroughly tested this stage.
 * <p/>
 * - Compute the maxflow and mincut using the FordFulkerson data type. You can access the value of the flow with the value()
 * method; you can identify which vertices are on the source side of the mincut with the inCut() method.
 * <p/>
 * - The BaseballElimination API contains the public methods that you will implement. For modularity, you will want to add
 * some private helper methods of your own.
 * <p/>
 * Your program shouldn't be too long—ours is less than 200 lines. If things get complicated, take a step back and re-think your approach.
 */
public class BaseballElimination {

    private ST<Integer, String> st;
    private ST<String, Integer> ts;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;

    /**
     * 4
     * w   l  r
     * Atlanta       83 71  8  0 1 6 1
     * Philadelphia  80 79  3  1 0 0 2
     * New_York      78 78  6  6 0 0 0
     * Montreal      77 82  3  1 2 0 0
     * create a baseball division from given filename in format specified below
     */
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int tN = in.readInt();
        wins = new int[tN];
        losses = new int[tN];
        remaining = new int[tN];
        against = new int[tN][tN];

        st = new ST<Integer, String>();
        ts = new ST<String, Integer>();

        for (int row = 0; row < tN; row++) {

            String team = in.readString();
            if (!st.contains(row)) st.put(row, team);
            if (!ts.contains(team)) ts.put(team, row);

            wins[row] = in.readInt();
            losses[row] = in.readInt();
            remaining[row] = in.readInt();

            for (int col = 0; col < tN; col++)
                against[row][col] = in.readInt();

        }

        if (st.size() != tN) throw new java.lang.IllegalArgumentException();
    }

    /**
     * number of teams
     */
    public int numberOfTeams() {
        return st.size();
    }

    /**
     * all teams
     */
    public Iterable<String> teams() {
        return ts.keys();
    }

    /**
     * number of wins for given team
     */
    public int wins(String team) {
        if (!ts.contains(team))
            throw new IllegalArgumentException("Invalid team name: " + team);
        int i = ts.get(team);
        return wins[i];
    }

    /**
     * number of losses for given team
     */
    public int losses(String team) {
        if (!ts.contains(team))
            throw new IllegalArgumentException("Invalid team name: " + team);
        int i = ts.get(team);
        return losses[i];
    }

    /**
     * number of remaining games for given team
     */
    public int remaining(String team) {
        if (!ts.contains(team))
            throw new IllegalArgumentException("Invalid team name: " + team);
        int i = ts.get(team);
        return remaining[i];
    }

    /**
     * number of remaining games between team1 and team2
     */
    public int against(String team1, String team2) {
        if (!ts.contains(team1))
            throw new IllegalArgumentException("Invalid team name: " + team1);
        if (!ts.contains(team2))
            throw new IllegalArgumentException("Invalid team name: " + team2);
        int i = ts.get(team1);
        int j = ts.get(team2);
        return against[i][j];
    }

    private class TeamNetWork {
        private int tN;
        private int[][] map;
        private FordFulkerson ff;
        private SET<String> winners;

        public TeamNetWork(String team) {
            tN = numberOfTeams();
            int V = 1;
            int tId = ts.get(team);
            map = new int[tN][tN];
            for (int i = 0; i < tN; i++) {
                for (int j = i; j < tN; j++) {
                    if (i == tId || j == tId || against[i][j] == 0)
                        continue;
                    map[i][i] = V++;
                    map[j][j] = V++;
                    map[i][j] = V++;
                }
            }
            V++;

            FlowNetwork fNet = new FlowNetwork(V);

            for (int i = 0; i < tN; i++) {
                for (int j = i; j < tN; j++) {
                    if (map[i][j] == 0)
                        continue;
                    if (i == j) {
                        fNet.addEdge(new FlowEdge(map[i][i], V - 1, wins[tId] + remaining[tId] - wins[i]));
                    } else {
                        fNet.addEdge(new FlowEdge(0, map[i][j], against[j][i]));
                        fNet.addEdge(new FlowEdge(map[i][j], map[i][i], Double.POSITIVE_INFINITY));
                        fNet.addEdge(new FlowEdge(map[i][j], map[j][j], Double.POSITIVE_INFINITY));
                    }
                }
            }

            ff = new FordFulkerson(fNet, 0, V - 1);

            winners = new SET<String>();
            for (int i = 0; i < tN; i++) {
                if (map[i][i] != 0 && ff.inCut(map[i][i])) {
                    winners.add(st.get(i));
                }
            }
        }

        /**
         * @return true if eliminated
         */
        public boolean isEliminated() {
            return winners.size() > 0;
        }

        /**
         * @return elimination certificate
         */
        public Iterable<String> certificate() {
            if (winners.size() > 0)
                return winners;

            return null;
        }
    }

    /**
     * Trivial elimination. If the maximum number of games team x can win is less than the number of wins of some other team i,
     * then team x is trivially eliminated (as is Montreal in the example above). That is, if w[x] + r[x] < w[i],
     * then team x is mathematically eliminated
     * <p/>
     * is given team eliminated?
     */
    public boolean isEliminated(String team) {
        if (!ts.contains(team))
            throw new IllegalArgumentException("Invalid team name: " + team);

        SET<String> certificate = certificateOfTrivialElimination(team);

        if (certificate != null)
            return true;

        TeamNetWork tn = new TeamNetWork(team);
        return tn.isEliminated();
    }

    /**
     * subset R of teams that eliminates given team; null if not eliminated
     */
    public Iterable<String> certificateOfElimination(String team) {
        if (!ts.contains(team))
            throw new IllegalArgumentException("Invalid team name: " + team);

        SET<String> certificate = certificateOfTrivialElimination(team);

        if (certificate != null)
            return certificate;

        TeamNetWork tn = new TeamNetWork(team);
        return tn.certificate();
    }

    /**
     * @param team Team name
     * @return Teams set
     */
    private SET<String> certificateOfTrivialElimination(String team) {
        SET<String> winners = new SET<String>();
        for (String t : teams())
            if (wins(team) + remaining(team) < wins(t))
                winners.add(t);
        if (winners.size() > 0)
            return winners;

        return null;
    }

    /**
     * @param args Arguments
     */
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);

//        try {
//            division.wins("Princeton");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("wins ok: " + e.getMessage());
//        }
//
//        try {
//            division.losses("Princeton");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("losses ok");
//        }
//
//        try {
//            division.remaining("Princeton");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("remaining ok");
//        }
//
//        try {
//            division.isEliminated("Princeton");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("isEliminated ok");
//        }
//
//        try {
//            division.certificateOfElimination("Princeton");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("certificateOfElimination ok");
//        }
//
//        try {
//            division.against("Princeton", "Tigers");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("against ok");
//        }
//
//        try {
//            division.against("Princeton", "New_York");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("against ok");
//        }
//
//        try {
//            division.against("New_York", "Princeton");
//        } catch(IllegalArgumentException e) {
//            StdOut.println("against ok: " + e.getMessage());
//        }

//        StdOut.println(division.numberOfTeams());
//        for (String team : division.teams()) {
//            StdOut.println("---");
//            StdOut.println(team);
//            StdOut.println(division.wins(team));
//            StdOut.println(division.losses(team));
//            StdOut.println(division.remaining(team));
//            for (String other : division.teams()) {
//                StdOut.println(other);
//                StdOut.println(division.against(team, other));
//            }
//        }

        for (String team : division.teams()) {
            division.isEliminated(team);
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
