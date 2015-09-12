public interface QubicInterface
{
    // use the Constructor to perform any initializations

    // state is a 3D array representing the current state
    // entries are: 0 .. empty, 1 .. player1, -1 .. player2
    // playerToMove: this is you, either 1 or -1
    // maxDepth: the maximum depth you alpha-beta algorithm
    // is allowed to explore
    //
    // you return your selected move as an integer array with
    // three entries: x,y,z coordinate of the tile you want
    // to move to; coordinates are zero-based: 0,1,2,3 are the
    // only legal values for Qubic

    public int[] move(byte[][][] state, int playerToMove, int maxDepth);
}
