/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sudoku;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Random;

/**
 *
 * @author feligomeez
 */
public class SudokuImpl extends UnicastRemoteObject implements GestorJuegosIntf {

    public SudokuImpl() throws RemoteException {
        super();
    }

    private static int contador = 0;

    class TJuego {

        int Codigo; //Código del juego
        char Tablero[][] = new char[9][9]; //Tablero de juego
        TDificultad Dificultad = new TDificultad(); //Dificultad
    }

    TJuego Sudokus[] = new TJuego[30];
    int NSudokus = 0; //Número de juegos almacenados en el vector Sudokus

    public int existeJuego(int cod) {
        int pos = -1;
        int i = 0;
        boolean encontrado = false;
        while(!encontrado && i < NSudokus) {
            if (Sudokus[i].Codigo == cod) {
                encontrado = true;
                pos = i;
            }
            i++;
        }
        return pos;
    }


    public int BuscarPorFila(int pCodJuego, int pFil, char pVal) {
        int Cuantos = 0;
        int pos = existeJuego(pCodJuego);
        for (int c = 0; c < 9 && Cuantos < 2; c++) {
            if (Sudokus[pos].Tablero[pFil][c] == pVal) {
                Cuantos++;
            }
        }
        return Cuantos;
    }

    public int BuscarPorColumna(int pCodJuego, int pCol, char pVal) {
        int Cuantos = 0;
        int pos = existeJuego(pCodJuego);
        for (int f = 0; f < 9 && Cuantos < 2; f++) {
            if (Sudokus[pos].Tablero[f][pCol] == pVal) {
                Cuantos++;
            }
        }
        return Cuantos;
    }

    int BuscarPorBloque(int pCodJuego, int pFil, int pCol, char pVal) {
        int pos = existeJuego(pCodJuego);
        int minF = (pFil / 3) * 3;
        int minC = (pCol / 3) * 3;
        int maxF = minF + 3;
        int maxC = minC + 3;

        int Cuantos = 0;
        for (int f = minF; f < maxF && Cuantos < 2; f++) {
            for (int c = minC; c < maxC && Cuantos < 2; c++) {
                if (Sudokus[pos].Tablero[f][c] == pVal) {
                    Cuantos++;
                }
            }
        }
        return Cuantos;
    }

    public void Inicializar(TJuego juego, TDificultad pDifi) {
        juego.Codigo = contador++;
        int d = pDifi.getDificultad();
        juego.Dificultad.setDificultad(d);
        for (int f = 0; f < 9; f++) {
            for (int c = 0; c < 9; c++) {
                juego.Tablero[f][c] = ' ';
            }
        }        
        Sudokus[NSudokus] = juego;
        NSudokus++;
        
    }

    @Override
    public int NuevoJuego(TDificultad pDifi) throws RemoteException {
        Random r = new Random(System.currentTimeMillis());
        int f, c;
        char Valor;
        TJuego sudo = new TJuego();
        Inicializar(sudo, pDifi);

        char ValoresActuales[] = new char[81];

        for (int i = 0; i < 81; i++) {
            ValoresActuales[i] = ' ';
        }

        int Pos;
        for (char v = '1'; v <= '9'; v++) {
            Pos = r.nextInt(9);
            while (ValoresActuales[Pos] != ' ') {
                Pos++;
                if (Pos == 9) {
                    Pos = 0;
                }
            };
            ValoresActuales[Pos] = v;
            PonerValor(sudo.Codigo, 0, Pos, v);
        };

        Pos = 9;
        while (Pos < 81) {
            f = Pos / 9;
            c = Pos % 9;
            Valor = ValoresActuales[Pos] != ' ' ? ValoresActuales[Pos] : '0';

            boolean EsCorrecto = false;
            while (EsCorrecto == false && Valor < '9') {
                Valor++;
                PonerValor(sudo.Codigo, f, c, Valor);
                EsCorrecto = ComprobarValor(sudo.Codigo, f, c, Valor);
            };

            if (EsCorrecto == true) {
                ValoresActuales[Pos] = Valor;
                Pos++;
            } else {
                ValoresActuales[Pos] = ' ';
                PonerValor(sudo.Codigo, f, c, ' ');
                Pos--;
            }
        }

        int NHuecos = 0;
        switch (pDifi.getDificultad()) {
            case 1:
                NHuecos = 10;
                break;
            case 2:
                NHuecos = 30;
                break;
            case 4:
                NHuecos = 60;
                break;
            case 5:
                NHuecos = 70;
                break;
            default:
                NHuecos = 40;
        };

        for (int i = 0; i < NHuecos; i++) {
            do {
                f = r.nextInt(9);
                c = r.nextInt(9);
            } while (ObtenerValor(sudo.Codigo, f, c) == ' ');
            PonerValor(sudo.Codigo, f, c, ' ');
        }
        return sudo.Codigo;
    }

    @Override
    public boolean BorrarJuego(int pCodJuego) throws RemoteException {
        int pos = existeJuego(pCodJuego);
        if (pos != -1) {
            for (int i = pos + 1; i < NSudokus; i++) {
                Sudokus[i - 1] = Sudokus[i];
            }
            NSudokus--;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean PonerValor(int pCodJuego, int pFila, int pColumna, char pValor) throws RemoteException {
        boolean result = false;
        TJuego sud;
        int pos = existeJuego(pCodJuego);
        if (pos != -1) {
            Sudokus[pos].Tablero[pFila][pColumna] = pValor;
            result = true;
        }
        return result;
    }

    @Override
    public char ObtenerValor(int pCodJuego, int pFila, int pColumna) throws RemoteException {
        TJuego sud;
        int pos = existeJuego(pCodJuego);
        if (pos != -1) {
            return Sudokus[pos].Tablero[pFila][pColumna];
        } else {
            return '-';
        }
    }

    @Override
    public boolean ComprobarValor(int pCodJuego, int pFila, int pColumna, char pValor) throws RemoteException {
        boolean Salida = true;
        if ((BuscarPorFila(pCodJuego, pFila, pValor) != 1 || BuscarPorColumna(pCodJuego, pColumna, pValor) != 1 || BuscarPorBloque(pCodJuego, pFila, pColumna, pValor) != 1)) {
            Salida = false;
        }
        return Salida;
    }

    @Override
    public int NumeroHuecos(int pCodJuego) throws RemoteException {

        int Cuantos = 0;
        int pos = existeJuego(pCodJuego);
        if (pos != -1) {
            for (int f = 0; f < 9; f++) {
                for (int c = 0; c < 9; c++) {
                    if (Sudokus[pos].Tablero[f][c] == ' ') {
                        Cuantos++;
                    }
                }
            }
        } else {
            return 81;
        }

        return Cuantos;

    }

    @Override
    public String Ayuda(int pCodJuego, int pFila, int pColumna) throws RemoteException {
        char Valor;
        String pAyu = "";
        char v = ObtenerValor(pCodJuego, pFila, pColumna);
        if (v == ' ') {
            Valor = '1';
            while (Valor <= '9') {
                PonerValor(pCodJuego, pFila, pColumna, Valor);
                if (ComprobarValor(pCodJuego, pFila, pColumna, Valor) == true) {
                    pAyu += Valor;
                    pAyu += ' ';
                }
                Valor++;
            }
            PonerValor(pCodJuego, pFila, pColumna, ' ');
        }
        return pAyu;
    }

    @Override
    public boolean Correcto(int pCodJuego) throws RemoteException {
        boolean Salida = true;
        char Valor = '1';
        int f, c;
        while (Valor <= '9' && Salida == true) {
            c = 0;
            while (c < 9 && Salida == true) {
                if (BuscarPorColumna(pCodJuego, c, Valor) != 1) {
                    Salida = false;
                }
                c++;
            };
            f = 0;
            while (f < 9 && Salida == true) {
                if (BuscarPorFila(pCodJuego, f, Valor) != 1) {
                    Salida = false;
                }
                f++;
            };

            for (f = 0; f < 9 && Salida == true; f += 3) {
                for (c = 0; c < 9 && Salida == true; c += 3) {
                    if (BuscarPorBloque(pCodJuego, f, c, Valor) != 1) {
                        Salida = false;
                    }
                }
            }
            Valor++;
        }
        return Salida;

    }

    @Override
    public String GetSudoku(int pCodJuego) throws RemoteException {
        int numero;
        String pSudo = "";
        int pos = existeJuego(pCodJuego);
        pSudo += "Dificultad: ";
        switch (pos<0?-1:Sudokus[pos].Dificultad.getDificultad()) {
            case 1:
                pSudo += "Muy Fácil";
                break;
            case 2:
                pSudo += "Fácil";
                break;
            case 3:
                pSudo += "Media";
                break;
            case 4:
                pSudo += "Difícil";
                break;
            case 5:
                pSudo += "Muy Difícil";
                break;
            default:
                pSudo += "** Sudoku vacío **";
        }

        numero = NumeroHuecos(pCodJuego);
        pSudo += "\tHuecos: " + numero + "\n 123 456 789\n";
        for (int f = 0; f < 9; f++) {
            if (f % 3 == 0) {
                pSudo += " +---+---+---+\n";
            }

            numero = f + 1;
            pSudo += numero;
            for (int c = 0; c < 9; c++) {
                if (c % 3 == 0) {
                    pSudo += "|";
                }
                pSudo += ObtenerValor(pCodJuego, f, c);
            };
            pSudo += "|\n";
        };
        pSudo += "+---+---+---+\n";
        return pSudo;
    }

}
