import java.util.Random;
import java.util.Scanner;

public class Battleship {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int boardSize = 10;
        int numLanchas = 0;
        int numBuques = 0;
        int numAcorazados = 0;
        int numPortaaviones = 0;
        int numIntentos = 50;

        printMenu();
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                numLanchas = 5;
                numBuques = 3;
                numAcorazados = 1;
                numPortaaviones = 1;
                break;
            case 2:
                numLanchas = 2;
                numBuques = 1;
                numAcorazados = 1;
                numPortaaviones = 1;
                numIntentos = 30;
                break;
            case 3:
                numLanchas = 1;
                numBuques = 1;
                numIntentos = 10;
                break;
            case 4:
                System.out.println("Introduce el tamaño del tablero:");
                boardSize = scanner.nextInt();
                System.out.println("Número de lanchas:");
                numLanchas = scanner.nextInt();
                System.out.println("Número de buques:");
                numBuques = scanner.nextInt();
                System.out.println("Número de acorazados:");
                numAcorazados = scanner.nextInt();
                System.out.println("Número de portaaviones:");
                numPortaaviones = scanner.nextInt();
                System.out.println("Número de intentos:");
                numIntentos = scanner.nextInt();
                break;
            default:
                System.out.println("Opción no válida. Jugando en nivel Fácil por defecto.");
                numLanchas = 5;
                numBuques = 3;
                numAcorazados = 1;
                numPortaaviones = 1;
                numIntentos = 50;
                break;
        }

        char[][] hiddenBoard = generateBoard(boardSize);
        char[][] visibleBoard = generateBoard(boardSize);
        setAleatoryShips(hiddenBoard, numLanchas, numBuques, numAcorazados, numPortaaviones);

        int attempts = 0;
        int remainingShips = countRemainingShips(hiddenBoard);

        System.out.println("¡Empiezas con " + remainingShips + " barcos!");

        while (attempts < numIntentos && remainingShips > 0) {
            showBoard(visibleBoard);
            System.out.println("Ingresa la coordenada para disparar (Ejemplo: A2):");
            String shoot = scanner.next().toUpperCase();

            int row = shoot.charAt(0) - 'A';
            int column = Integer.parseInt(shoot.substring(1)) - 1;

            if (row >= 0 && row < boardSize && column >= 0 && column < boardSize) {
                boolean shootResult = manageShoot(row, column, hiddenBoard, visibleBoard);
                if (shootResult) {
                    remainingShips = countRemainingShips(hiddenBoard); // Actualizar barcos restantes
                    System.out.println("¡Tocado!");
                } else {
                    System.out.println("¡Agua!");
                }
            } else {
                System.out.println("Coordenada inválida. Introduce una coordenada válida (Ejemplo: A5).");
            }

            attempts++;
        }

        if (remainingShips == 0) {
            System.out.println("¡Has ganado! Has hundido todos los barcos.");
        } else {
            System.out.println("¡Has perdido! No pudiste hundir todos los barcos en tus intentos.");
        }
        showBoard(hiddenBoard);
    }
    // Función para el menú principal
    public static void printMenu() {
        System.out.println("¡Bienvenido a Hundir la Flota!");
        System.out.println("Escoge el nivel de dificultad:");
        System.out.println("1. Fácil");
        System.out.println("2. Medio");
        System.out.println("3. Difícil");
        System.out.println("4. Personalizado");
    }

    // Función para generar un tablero vacío del tamaño especificado
    public static char[][] generateBoard(int size) {
        return new char[size][size];
    }

    // Función para colocar barcos aleatoriamente en el tablero oculto
    public static void setAleatoryShips(char[][] board, int numLanchas, int numBuques, int numAcorazados, int numPortaaviones) {
        setShips(board, numLanchas, 'L');
        setShips(board, numBuques, 'B');
        setShips(board, numAcorazados, 'Z');
        setShips(board, numPortaaviones, 'P');
    }

    // Función para colocar barcos en el tablero
    public static void setShips(char[][] board, int amount, char shipType) {
        Random rand = new Random();
        int shipSize = getShipSize(shipType);

        for (int i = 0; i < amount; i++) {
            boolean settedShip = false;

            while (!settedShip) {
                int row = rand.nextInt(board.length);
                int column = rand.nextInt(board[0].length);
                boolean isHorizontal = rand.nextBoolean();

                if (checkAvailableSpace(board, row, column, shipSize, isHorizontal)) {
                    setShipInBoard(board, row, column, shipSize, isHorizontal, shipType);
                    settedShip = true;
                }
            }
        }
    }

    // Función para obtener el tamaño de un barco según su tipo
    public static int getShipSize(char shipType) {
        if (shipType == 'L') {
            return 1;
        } else if (shipType == 'B') {
            return 3;
        } else if (shipType == 'Z') {
            return 4;
        } else if (shipType == 'P') {
            return 5;
        }
        return 0;
    }

    // Función para verificar si hay espacio disponible para colocar un barco
    public static boolean checkAvailableSpace(char[][] board, int row, int column, int shipSize, boolean isHorizontal) {
        int size = board.length;

        if (isHorizontal) {
            if (column + shipSize > size) {
                return false;
            }

            for (int j = column; j < column + shipSize; j++) {
                if (board[row][j] != 0 && board[row][j] != '-') {
                    return false;
                }
            }
        } else {
            if (row + shipSize > size) {
                return false;
            }

            for (int i = row; i < row + shipSize; i++) {
                if (board[i][column] != 0 && board[i][column] != '-') {
                    return false;
                }
            }
        }
        return true;
    }

    // Función para colocar un barco en el tablero
    public static void setShipInBoard(char[][] board, int row, int column, int shipSize, boolean isHorizontal, char shipType) {
        if (isHorizontal) {
            for (int j = column; j < column + shipSize; j++) {
                board[row][j] = shipType;
            }
        } else {
            for (int i = row; i < row + shipSize; i++) {
                board[i][column] = shipType;
            }
        }
    }

    // Función para procesar el disparo y actualizar los tableros
    public static boolean manageShoot(int row, int column, char[][] hiddenBoard, char[][] visibleBoard) {
        if (visibleBoard[row][column] == 'X' || visibleBoard[row][column] == 'A') {
            System.out.println("Ya has disparado en esta coordenada. Inténtalo de nuevo.");
            return false;
        } else if (hiddenBoard[row][column] == 'L' || hiddenBoard[row][column] == 'B' || hiddenBoard[row][column] == 'Z' || hiddenBoard[row][column] == 'P') {
            char shipType = hiddenBoard[row][column];
            hiddenBoard[row][column] = 'X'; // Marcamos la posición en el tablero oculto como 'tocado'

            boolean isSunk = isShipSunk(hiddenBoard, shipType);

            if (isSunk) {
                markSunkShip(visibleBoard, shipType);
                System.out.println("¡Hundido!");
            } else {
                visibleBoard[row][column] = 'X'; // Tocado en el tablero visible
                System.out.println("¡Tocado!");
            }

            return true;
        } else {
            visibleBoard[row][column] = 'A'; // Agua
            System.out.println("¡Agua!");
            return false;
        }
    }
    //Funcion para comprobar si el barco se ha hundido
    public static boolean isShipSunk(char[][] hiddenBoard, char shipType) {
        for (int i = 0; i < hiddenBoard.length; i++) {
            for (int j = 0; j < hiddenBoard[i].length; j++) {
                if (hiddenBoard[i][j] == shipType) {
                    return false; // El barco aún tiene partes no tocadas
                }
            }
        }
        return true; // Todas las partes del barco han sido tocadas (barco hundido)
    }
    public static void markSunkShip(char[][] visibleBoard, char shipType) {
        for (int i = 0; i < visibleBoard.length; i++) {
            for (int j = 0; j < visibleBoard[i].length; j++) {
                if (visibleBoard[i][j] == shipType) {
                    visibleBoard[i][j] = 'X';
                }
            }
        }
    }
    // Función para contar la cantidad de barcos restantes en el tablero oculto
    public static int countRemainingShips(char[][] board) {
        int count = 0;
        for (char[] row : board) {
            for (char cell : row) {
                if (cell != '-' && cell != 'X') {
                    count++;
                }
            }
        }
        return count;
    }

    // Función para mostrar el tablero visible al jugador
    public static void showBoard(char[][] board) {
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        System.out.println("  ---------------------");
        char rowLabel = 'A';
        for (char[] row : board) {
            System.out.print(rowLabel + "| ");
            for (char cell : row) {
                if (cell == 0 || cell == 'X' || cell == 'A') {
                    System.out.print(cell + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
            rowLabel++;
        }
        System.out.println();
    }
}
