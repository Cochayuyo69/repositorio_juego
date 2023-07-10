package com.mycompany.juego;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;

public class JuegoLaberinto extends JFrame implements KeyListener {

    private static final int ANCHO_VENTANA = 800;
    private static final int ALTO_VENTANA = 600;
    private static final int TAMANIO_CELDA = 40;
    private static final int ANCHO_TABLERO = ANCHO_VENTANA / TAMANIO_CELDA;
    private static final int ALTO_TABLERO = ALTO_VENTANA / TAMANIO_CELDA;

    private char[][] tablero;
    private int jugadorX;
    private int jugadorY;
    private int inicioX;
    private int inicioY;
    private int finX;
    private int finY;

    public JuegoLaberinto() {
        tablero = new char[ANCHO_TABLERO][ALTO_TABLERO];
        jugadorX = 1;
        jugadorY = 1;

        // Generar laberinto aleatorio
        generarLaberinto();

        // Colocar al jugador en el inicio del laberinto
        jugadorX = inicioX;
        jugadorY = inicioY;
        tablero[jugadorX][jugadorY] = '@';

        // Configurar la ventana del juego
        setTitle("Juego de Laberinto");
        setSize(ANCHO_VENTANA, ALTO_VENTANA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(this);
        setVisible(true);
    }

    private void generarLaberinto() {
        // Inicializar el tablero
        for (int i = 0; i < ANCHO_TABLERO; i++) {
            for (int j = 0; j < ALTO_TABLERO; j++) {
                tablero[i][j] = '#'; // Pared
            }
        }

        // Generar laberinto utilizando el algoritmo de Recursive Backtracking
        Random random = new Random();
        generarLaberintoRecursivo(random, 1, 1);

        // Generar puntos de inicio y fin
        generarPuntosInicioFin(random);
    }

    private void generarLaberintoRecursivo(Random random, int x, int y) {
        tablero[x][y] = '.';

        int[] direcciones = {1, 2, 3, 4};
        shuffleArray(direcciones, random);

        for (int direccion : direcciones) {
            int nx = x;
            int ny = y;

            switch (direccion) {
                case 1: // Arriba
                    ny -= 2;
                    break;
                case 2: // Derecha
                    nx += 2;
                    break;
                case 3: // Abajo
                    ny += 2;
                    break;
                case 4: // Izquierda
                    nx -= 2;
                    break;
            }

            if (nx > 0 && nx < ANCHO_TABLERO - 1 && ny > 0 && ny < ALTO_TABLERO - 1 && tablero[nx][ny] == '#') {
                int mx = x + (nx - x) / 2;
                int my = y + (ny - y) / 2;

                tablero[mx][my] = '.';
                generarLaberintoRecursivo(random, nx, ny);
            }
        }
    }

    private void generarPuntosInicioFin(Random random) {
        inicioX = random.nextInt(ANCHO_TABLERO - 2) + 1;
        inicioY = random.nextInt(ALTO_TABLERO - 2) + 1;

        do {
            finX = random.nextInt(ANCHO_TABLERO - 2) + 1;
            finY = random.nextInt(ALTO_TABLERO - 2) + 1;
        } while (inicioX == finX && inicioY == finY);

        tablero[inicioX][inicioY] = 'I'; // Punto de inicio
        tablero[finX][finY] = 'F'; // Punto de fin
    }

    private void shuffleArray(int[] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < ANCHO_TABLERO; i++) {
            for (int j = 0; j < ALTO_TABLERO; j++) {
                if (tablero[i][j] == '#') {
                    g.setColor(Color.GRAY);
                    g.fillRect(i * TAMANIO_CELDA, j * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                } else if (tablero[i][j] == '.') {
                    g.setColor(Color.WHITE);
                    g.fillRect(i * TAMANIO_CELDA, j * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                } else if (tablero[i][j] == '@') {
                    g.setColor(Color.GREEN);
                    g.fillRect(i * TAMANIO_CELDA, j * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                } else if (tablero[i][j] == 'I') {
                    g.setColor(Color.BLUE);
                    g.fillRect(i * TAMANIO_CELDA, j * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                } else if (tablero[i][j] == 'F') {
                    g.setColor(Color.RED);
                    g.fillRect(i * TAMANIO_CELDA, j * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

        switch (tecla) {
            case KeyEvent.VK_UP:
                moverJugador(jugadorX, jugadorY - 1);
                break;
            case KeyEvent.VK_DOWN:
                moverJugador(jugadorX, jugadorY + 1);
                break;
            case KeyEvent.VK_LEFT:
                moverJugador(jugadorX - 1, jugadorY);
                break;
            case KeyEvent.VK_RIGHT:
                moverJugador(jugadorX + 1, jugadorY);
                break;
            default:
                break;
        }

        repaint();
    }

    private void moverJugador(int nuevoX, int nuevoY) {
        if (tablero[nuevoX][nuevoY] != '#') {
            tablero[jugadorX][jugadorY] = '.';
            jugadorX = nuevoX;
            jugadorY = nuevoY;
            tablero[jugadorX][jugadorY] = '@';
        }

        // Verificar si el jugador llegó al punto de fin
        if (jugadorX == finX && jugadorY == finY) {
            JOptionPane.showMessageDialog(this, "¡Felicidades! Has llegado al final del laberinto.");
            reiniciarJuego();
        }
    }

    private void reiniciarJuego() {
        tablero = new char[ANCHO_TABLERO][ALTO_TABLERO];
        generarLaberinto();
        jugadorX = inicioX;
        jugadorY = inicioY;
        tablero[jugadorX][jugadorY] = '@';
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JuegoLaberinto();
            }
        });
    }
}