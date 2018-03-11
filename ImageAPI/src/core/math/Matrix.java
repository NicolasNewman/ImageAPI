package core.math;

import java.util.Arrays;

import exception.MatrixException;

public class Matrix {
	
	private double[][] matrix;
	private int row;
	private int col;
	
	/*
	 *=====================
	 * Constructors
	 *=====================
	 *
	 */
	
	public Matrix(double[][] matrix) {
		this.matrix = matrix;
		row = matrix.length;
		col = matrix[0].length;
	}
	
	public Matrix(int row, int col, int val) {
		matrix = new double[row][col];
		this.row = row;
		this.col = col;
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				matrix[r][c] = val;
			}
		}
	}
	
	public Matrix(Matrix m2) {
		this.matrix = new double[m2.matrix.length][m2.matrix[0].length];
		for(int i = 0; i < this.matrix.length; i++) {
			for(int j = 0; j < this.matrix[0].length; j++) {
				this.matrix[i][j] = m2.getValue(i, j);
			}
		}
		this.row = m2.row;
		this.col = m2.col;
	}
	
	/*
	 *=====================
	 * Arithmetic Functions
	 *=====================
	 *
	 */

	public Matrix add(Matrix m2) throws MatrixException {
		double[][] tempMatrix = new double[row][col];
		if(row == m2.getRow() && col == m2.getCol()) {
			for(int r = 0; r < row; r++) {
				for(int c = 0; c < col; c++) {
					tempMatrix[r][c] = matrix[r][c] + m2.getValue(r, c);
				}
			}
		} else {
			throw new MatrixException("Error: The sizes don't match");
		}
		return new Matrix(tempMatrix);
	}
	
	public static Matrix add(Matrix m1, Matrix m2) throws MatrixException {
		double[][] tempMatrix = new double[m1.getRow()][m1.getCol()];
		if(m1.getRow() == m2.getRow() && m1.getCol() == m2.getCol()) {
			for(int r = 0; r < m1.getRow(); r++) {
				for(int c = 0; c < m1.getCol(); c++) {
					tempMatrix[r][c] = m1.getValue(r, c) + m2.getValue(r, c);
				}
			}
		} else {
			throw new MatrixException("Error: The sizes don't match");
		}
		Matrix m = new Matrix(tempMatrix);
		return m;
	}
	
	public Matrix subtract(Matrix m2) throws MatrixException {
		double[][] tempMatrix = new double[row][col];
		if(row == m2.getRow() && col == m2.getCol()) {
			for(int r = 0; r < row; r++) {
				for(int c = 0; c < col; c++) {
					tempMatrix[r][c] = matrix[r][c] - m2.getValue(r, c);
				}
			}
		} else {
			throw new MatrixException("Error: The sizes don't match");
		}
		return new Matrix(tempMatrix);
	}
	
	public static Matrix subtract(Matrix m1, Matrix m2) throws MatrixException {
		double[][] tempMatrix = new double[m1.getRow()][m1.getCol()];
		if(m1.getRow() == m2.getRow() && m1.getCol() == m2.getCol()) {
			for(int r = 0; r < m1.getRow(); r++) {
				for(int c = 0; c < m1.getCol(); c++) {
					tempMatrix[r][c] = m1.getValue(r, c) - m2.getValue(r, c);
				}
			}
		} else {
			throw new MatrixException("Error: The sizes don't match");
		}
		Matrix m = new Matrix(tempMatrix);
		return m;
	}
	
	public Matrix multiply(Matrix m2) throws MatrixException {
		if(canMultiply(m2)) {
			double[][] m3 = new double[col][m2.getRow()];
			int m1Col = col;
			int m3Row = m3.length;
			int m3Col = m3[0].length;
			for(int m3r = 0; m3r < m3Row; m3r++) {
				for(int m3c = 0; m3c < m3Col; m3c++) {
					for(int m1c = 0; m1c < m1Col; m1c++) {
						m3[m3r][m3c] += matrix[m3r][m1c] * m2.getValue(m1c, m3c);
					}
				}
			}
			return new Matrix(m3);
		} else {
			throw new MatrixException("Error: m1 row doesn't equal m2 col");
		}
	}
	
	public static Matrix multiply(Matrix m1, Matrix m2) throws MatrixException {
		if(canMultiply(m1, m2)) {
			double[][] m3 = new double[m1.getCol()][m2.getRow()];
			int m1Col = m1.getRow();
			int m3Row = m3.length;
			int m3Col = m3[0].length;
			for(int m3r = 0; m3r < m3Row; m3r++) {
				for(int m3c = 0; m3c < m3Col; m3c++) {
					for(int m1c = 0; m1c < m1Col; m1c++) {
						m3[m3r][m3c] += m1.getValue(m3r, m1c) * m2.getValue(m1c, m3c);
					}
				}
			}
			return new Matrix(m3);
		} else {
			throw new MatrixException("Error: m1 row doesn't equal m2 col");
		}
	}
	
	public Matrix multiplyByScalar(double scalar) {
		double temp[][] = new double[row][col];
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				temp[r][c] = matrix[r][c] * scalar;
			}
		}
		return new Matrix(temp);
	}
	
	public static Matrix multiplyByScalar(Matrix m, double scalar) {
		double[][] tempMatrix = new double[m.getRow()][m.getCol()];
		int row = m.getRow();
		int col = m.getCol();
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				tempMatrix[r][c] = m.getValue(r, c)*scalar;
			}
		}
		return new Matrix(tempMatrix);
	}
	
	public Matrix transpose() {
		int m2r = col;
		int m2c = row;
		double[][] transposed = new double[m2r][m2c];
		for(int r = 0; r < m2r; r++) {
			for(int c = 0; c < m2c; c++) {
				transposed[c][r] = matrix[r][c];
			}
		}
		return new Matrix(transposed);
	}
	
	public static Matrix transpose(Matrix m1) {
		int m2r = m1.getCol();
		int m2c = m1.getRow();
		double[][] transposed = new double[m2r][m2c];
		for(int r = 0; r < m2r; r++) {
			for(int c = 0; c < m2c; c++) {
				transposed[c][r] = m1.getValue(r, c);
			}
		}
		return new Matrix(transposed);
	}
	
	/*
	 *=====================
	 * Helper Functions
	 *=====================
	 *
	 */
	
	public double getValue(int r, int c) {
		return matrix[r][c];
	}
	
	public boolean canMultiply(Matrix m2) {
		return col == m2.getRow();
	}
	
	public static boolean canMultiply(Matrix m1, Matrix m2) {
		return m1.getCol() == m2.getRow();
	}
	
	public int[] getMultipliedDimensions(Matrix m2) {
		return new int[] {col, m2.getRow()};
	}
	
	public static int[] getMultipliedDimensions(Matrix m1, Matrix m2) {
		return new int[] {m1.getCol(), m2.getRow()};
	}
	
	public void printMatrix() {
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				System.out.print(matrix[r][c] + " ");
			}
			System.out.println("");
		}
	}
	
	public static Matrix generateIdentity(int n) {
		Matrix temp = new Matrix(n, n, 0);
		int placeOne = 0;
		for(int r = 0; r < temp.getRow(); r++) {
			for(int c = 0; c < temp.getCol(); c++) {
				if(r == placeOne && c == placeOne) {
					temp.setValue(r, c, 1);
				}
			}
			placeOne++;
		}
		return temp;
	}
	
	
	private double[][] divideRowByScalar(double[][] m, int r, double s) {
		for(int c = 0; c < m.length; c++) {
			m[r][c] /= s;
		}
		return m;
	}
	
	private double[][] copy(double[][] template) {
		double[][] temp = new double[template.length][template[0].length];
		for(int r = 0; r < template.length; r++) {
			for(int c = 0; c < template[0].length; c++) {
				temp[r][c] = matrix[r][c];
			}
		}
		return temp;
	}
	
	private double[][] identity(int s) {
		int i = 0;
		double[][] temp = new double[s][s];
		for(int r = 0; r < s; r++) {
			for(int c = 0; c < s; c++) {
				if(c == i) {
					temp[r][c] = 1;
				} else {
					temp[r][c] = 0;
				}
			}
			i++;
		}
		return temp;
	}
	
	public Matrix inverse() throws MatrixException{
		double[][] m = copy(matrix);
		double[][] im = identity(m.length);
		int i = 0;
		for(int r = 0; r < m.length; r++) {
			
			im = divideRowByScalar(im, r, m[r][i]);
			m = divideRowByScalar(m, r, m[r][i]);

			for(int r2 = 0; r2 < m[0].length; r2++) {
				if(r2 != r) {
				
					// r is the row that is being analyzed
					// r2 is the row that is being compared
					// c is the column
					double f = m[r2][i];
					for(int c = 0; c < m[0].length; c++) {
						im[r2][c] = im[r2][c] - m[r2][i] * im[r][c];
					}
					for(int c = 0; c < m[0].length; c++) {
						m[r2][c] = m[r2][c] - f * m[r][c];						
					}
				}
			}
			i++;
		}
		
		for(int r = 0; r < im.length; r++) {
			for(int c = 0; c < im[0].length; c++) {
				if(Double.isNaN(im[r][c])) {
					throw new MatrixException("Error: Matrix doesn't have inverse");
				}
				im[r][c] = Math.round(im[r][c]*100.)/100.;
			}
		}
		
		Matrix temp = new Matrix(im);
		return temp;
	}

	/*
	 *=====================
	 * Getters and Setters
	 *=====================
	 *
	 */
	
	public int getRow() { return row; }
	
	public int getCol() { return col; }
	
	public double[][] getMatrix() { return matrix; }
	
	public void setValue(int r, int c, double value) { matrix[r][c] = value; }
}
