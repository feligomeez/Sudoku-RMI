/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Sudoku;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.Scanner;

/**
 *
 * @author feligomeez
 */
public class RMI_Sudoku_Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException {
        Registry registry;
        try {
            int Puerto = 0;
            GestorJuegosIntf sudStub = new SudokuImpl();
            Scanner Teclado = new Scanner(System.in);
            System.out.print("Introduce el nº de puerto para comunicarse: ");
            Puerto = Teclado.nextInt();

            registry = LocateRegistry.createRegistry(Puerto);
            SudokuImpl obj = new SudokuImpl();

            registry = LocateRegistry.getRegistry(Puerto);
            registry.rebind("GestorJuegosIntf", obj);
            System.out.println("Servidor Sudoku escuchando...");

        } catch (RemoteException e) {
            System.out.println("Error en servidor Sudoku:" + e);
        }
    }

}
