/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Sudoku;

import java.net.MalformedURLException;
import java.util.Scanner;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author feligomeez
 */
public class RMI_Sudoku_Cliente {

    public static int MenuPrincipal(boolean pInicio) {
        Scanner sc = new Scanner(System.in);
        int valor;

        if (pInicio) {
            System.out.println("1.- Juego Nuevo\n");
            System.out.println("2.- -----------\n");
            System.out.println("3.- -----------\n");
            System.out.println("4.- -----------\n");
            System.out.println("5.- -----------\n");
        } else {
            System.out.println("1.- -----------\n");
            System.out.println("2.- Borrar Juego\n");
            System.out.println("3.- Poner Valor\n");
            System.out.println("4.- Borrar Valor\n");
            System.out.println("5.- Ayuda\n");
        }
        System.out.println("6.- Salir\n");
        System.out.println("Elige Opción: ");
        valor = sc.nextInt();

        return valor;
    }

    public static int MenuDificultad() {
        Scanner sc = new Scanner(System.in);
        int valor;
        do {
            System.out.println("Elige la Dificultad:\n");
            System.out.println("Muy Fácil(1) Fácil(2) Medio(3) Difícil(4) Muy Difícil(5)");
            System.out.println("Elige Opción: ");
            valor = sc.nextInt();
            if (valor < 1 || valor > 5) {
                System.out.println("** Error, elija una dificultad correcta.");
            }
        } while (valor < 1 || valor > 5);
        return valor;
    }

    public static void main(String[] args) throws NotBoundException, RemoteException, MalformedURLException {
        Scanner sc = new Scanner(System.in);
        String host = "localhost";
        int Puerto = 0;
        System.out.print("Introduce el nº de puerto para comunicarse: ");
        Puerto = sc.nextInt();
        Random rnd = new Random(System.nanoTime());
        System.out.println("Inicia cliente...");
        GestorJuegosIntf sudStub = (GestorJuegosIntf) Naming.lookup("rmi://" + host + ":" + Puerto + "/GestorJuegosIntf");

        int Menu;
        boolean Inicio = true;
        int F, C, Aviso = 0;
        char V, sn;
        int codigo = -1;
        String SudoActual, Ayuda;

        do {
            SudoActual = sudStub.GetSudoku(codigo);
            System.out.println(SudoActual);
            if (sudStub.NumeroHuecos(codigo) == 0) {
                System.out.println("\"*** SUDOKU COMPLETO \"");
                if (sudStub.Correcto(codigo) == false) {
                    System.out.println("PERO IN");
                }
                System.out.println("CORRECTO ***\n");
            }
            System.out.println("");
            Menu = MenuPrincipal(Inicio);

            switch (Menu) {
                case 1:
                    if (Inicio == true) {
                        TDificultad difi = new TDificultad();
                        difi.setDificultad(MenuDificultad());
                        codigo = sudStub.NuevoJuego(difi);
                        Inicio = false;
                    } else {
                        System.out.println("La opción " + Menu + " no está en el menú.");
                    }
                    break;
                case 2:
                    if (Inicio == false) {
                        System.out.println("¿ Está seguro de borrar el juego (s/n):");
                        sn = sc.next().charAt(0);
                        if (sn == 's' || sn == 'S') {
                            sudStub.BorrarJuego(codigo);
                            Inicio = true;
                        }
                    } else {
                        System.out.println("La opción " + Menu + " no está en el menú.");
                    }
                    break;
                case 3:
                    if (Inicio == false) {
                        System.out.println("Introduce Fila, Columna y Valor: ");
                        F = sc.nextInt();
                        C = sc.nextInt();
                        V = sc.next().charAt(0);
                        if (V < '1' || V > '9') {
                            Aviso = 5;
                        } else if (F < 1 || F > 9 || C < 1 || C > 9) {
                            System.out.println("La posición F=" + F + " C=" + C + " introducida es incorrecta.");
                        } else if (sudStub.ObtenerValor(codigo, F - 1, C - 1) != ' ') {
                            System.out.println("La posición F=" + F + " C=" + C + " está ocupada");
                        } else {
                            sudStub.PonerValor(codigo, F - 1, C - 1, V);
                            if (sudStub.ComprobarValor(codigo, F - 1, C - 1, V) == false) {
                                System.out.println("El valor '" + V + "' no se puede poner en la posición F=" + F + " C=" + C);
                            } else {
                                Aviso = 0;
                            }
                        }
                    } else {
                        System.out.println("La opción " + Menu + " no está en el menú.");
                    }
                    break;
                case 4:
                    if (Inicio == false) {
                        System.out.println("Introduce Fila, y Columna: ");
                        F = sc.nextInt();
                        C = sc.nextInt();
                        if (F < 1 || F > 9 || C < 1 || C > 9) {
                            System.out.println("La posición F=" + F + " C=" + C + " introducida es incorrecta.");
                        } else {
                            sudStub.PonerValor(codigo, F - 1, C - 1, ' ');
                        }
                    } else {
                        System.out.println("La opción " + Menu + " no está en el menú.");
                    }
                    break;
                case 5:
                    if (Inicio == false) {
                        System.out.println("Introduce Fila y Columna a obtener ayuda: ");
                        F = sc.nextInt();
                        C = sc.nextInt();
                        if (F < 1 || F > 9 || C < 1 || C > 9 || sudStub.ObtenerValor(codigo, F - 1, C - 1) != ' ') {
                            System.out.println("La posición F=" + F + " C=" + C + " introducida es incorrecta.");
                        } else {
                            Ayuda = sudStub.Ayuda(codigo, F - 1, C - 1);
                            System.out.println("Los posibles valores en la posición F=" + F + " C=" + C + " son: " + Ayuda);
                        }
                    } else {
                        System.out.println("La opción " + Menu + " no está en el menú.");
                    }
                case 6:
                    break;
                default:
                    System.out.println("La opción " + Menu + " no está en el menú.");
            };

            
        } while (Menu != 6);

    }

}
