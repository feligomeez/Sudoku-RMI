/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sudoku;

import java.io.Serializable;

/**
 *
 * @author feligomeez
 */
public class TDificultad implements Serializable {

    private int difi;

    public TDificultad() {
        difi = -1;
    }

    public void setDificultad(int d) {
        this.difi = d;
    }

    public int getDificultad() {
        return difi;
    }
}
