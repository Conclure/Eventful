public class Matrix<T> {
    private final Object[][] table;
    private final int rows;
    private final int columns;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.table = new Object[columns][rows];
    }

    public int columns() {
        return this.columns;
    }

    public int rows() {
        return this.rows;
    }

    public T set(int row, int column, T obj) {
        T oldValue = (T) this.table[column][row];
        this.table[column][row] = obj;
        return oldValue;
    }

    public T get(int row, int column) {
        return (T) this.table[column][row];
    }
}