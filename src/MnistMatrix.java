public class MnistMatrix {

    private int [][] data;

    private int nRows;
    private int nCols;

    private int label;
    private int guess;

    public MnistMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;

        data = new int[nRows][nCols];
    }

    public int getValue(int r, int c) {
        return data[r][c];
    }

    public void setValue(int row, int col, int value) {
        data[row][col] = value;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public int getNumberOfRows() {
        return nRows;
    }

    public int getNumberOfColumns() {
        return nCols;
    }

    public void radicalizeVals(int limit) {
        for(int x = 0; x < data.length; x++) {
            for(int y = 0; y < data[0].length; y++) {
                if(data[x][y] < limit) {
                    data[x][y] = 0;
                }
                else {
                    data[x][y] = 1;
                }
            }
        }
    }

}
