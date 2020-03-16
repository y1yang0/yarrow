package com.kelthuzadx.yarrow.test;


public class MatrixTest {
    public static int[][] fillMatrix(int[][] M) {
        for (int xx = 0; xx < 100000; xx++) {
            int ml = 0;
            for (int i = 0; i < M.length; i++) {
                ml = ml < M[i].length ? M[i].length : ml;
            }
            int[][] Nm = new int[M.length][ml];
            for (int i = 0; i < M.length; i++) {
                for (int j = 0; j < M[i].length; j++) {
                    Nm[i][j] = M[i][j];
                }
            }
            return Nm;
        }
        return null;
    }

    public static int[][] multiplication(int[][] A, int[][] B) {
        for (int xx = 0; xx < 100000; xx++) {
            A = fillMatrix(A);
            B = fillMatrix(B);

            int[][] C = new int[A.length][B[0].length];
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < B[i].length; j++) {
                    for (int k = 0; k < A[i].length; k++) {
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }
            return C;
        }
        return null;
    }

    public static int[][] transposed(int[][] A) {
        for (int xx = 0; xx < 100000; xx++) {
            A = fillMatrix(A);
            int[][] AT = new int[A[0].length][A.length];
            for (int i = 0; i < AT.length; i++) {
                for (int j = 0; j < AT[0].length; j++) {
                    AT[i][j] = A[j][i];
                }
            }
            return AT;
        }
        return null;
    }

    public static void work() {
        int[][] A = {{1, 2, 0}, {3, 4, 0}, {5, 6, 0}};
        int[][] B = {{1, 2, 3, 1}, {3, 2}, {}};
        int[][] C = multiplication(A, B);

        int[][] CT = transposed(C);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            work();
        }
    }
}
