import javax.swing.*;
import java.awt.*;

public class TicTacToe {

    private final JFrame frame = new JFrame();
    private final JLabel textLabel = new JLabel();
    private final JPanel textPanel = new JPanel();
    private final JPanel boardPanel = new JPanel();
    private final JPanel controlPanel = new JPanel();
    private final JButton[][] board = new JButton[3][3];
    private final JButton restartButton = new JButton("Restart");
    private final JButton pauseButton = new JButton("Pause");
    private final JButton exitButton = new JButton("Exit");

    private String playerX;
    private String playerO;
    private String currentPlayer;
    private boolean gameOver = false;
    private boolean gamePaused = false;
    private int turns = 0;

    public TicTacToe() {
        startMenu();
        int boardWidth = 600;
        int boardHeight = 650;
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setupTextPanel();
        setupBoardPanel();
        setupControlPanel();

        frame.add(textPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    private void setupTextPanel() {
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
    }

    private void setupBoardPanel() {
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton tile = new JButton();
                board[i][j] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener(_ -> handleTileClick(tile));
            }
        }
    }

    private void setupControlPanel() {
        restartButton.addActionListener(_ -> resetGame());
        pauseButton.addActionListener(_ -> togglePause());
        exitButton.addActionListener(_ -> System.exit(0));

        controlPanel.setLayout(new GridLayout(1, 3));
        controlPanel.add(restartButton);
        controlPanel.add(pauseButton);
        controlPanel.add(exitButton);
    }

    private void startMenu() {
        JFrame startFrame = new JFrame("Tic Tac Toe");
        startFrame.setSize(400, 300);
        startFrame.setLocationRelativeTo(null);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Tic Tac Toe", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.darkGray);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.add(title);

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));

        startButton.addActionListener(_ -> {
            startFrame.dispose();
            initializeGame();
        });

        exitButton.addActionListener(_ -> System.exit(0));

        JPanel buttonPanel = createButtonPanel(startButton, exitButton);

        startFrame.add(titlePanel, BorderLayout.CENTER);
        startFrame.add(buttonPanel, BorderLayout.SOUTH);
        startFrame.setVisible(true);
    }

    private JPanel createButtonPanel(JButton startButton, JButton exitButton) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        return buttonPanel;
    }

    private void initializeGame() {
        playerX = JOptionPane.showInputDialog(frame, "Enter Player 1 Name:", "Player X");
        playerO = JOptionPane.showInputDialog(frame, "Enter Player 2 Name:", "Player O");
        currentPlayer = playerX;

        textLabel.setText(currentPlayer + "'s turn");
        frame.setTitle("Tic Tac Toe");
        frame.setVisible(true);
    }

    private void handleTileClick(JButton clickedTile) {
        if (gameOver || gamePaused) return;
        if (clickedTile.getText().isEmpty()) {
            clickedTile.setText(currentPlayer.equals(playerX) ? "X" : "O");
            turns++;
            checkWinner();

            if (!gameOver) {
                currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                textLabel.setText(currentPlayer + "'s turn");
            }
        }
    }

    private void checkWinner() {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2]) ||
                    checkLine(board[0][i], board[1][i], board[2][i])) return;
        }

        if (checkLine(board[0][0], board[1][1], board[2][2]) ||
                checkLine(board[0][2], board[1][1], board[2][0])) return;

        if (turns == 9) {
            for (JButton[] row : board) {
                for (JButton tile : row) {
                    tile.setForeground(Color.orange);
                    tile.setBackground(Color.gray);
                }
            }
            gameOver = true;
            textLabel.setText("It's a tie!");
            textLabel.setForeground(Color.orange);

        }
    }

    private boolean checkLine(JButton a, JButton b, JButton c) {
        if (!a.getText().isEmpty() && a.getText().equals(b.getText()) && b.getText().equals(c.getText())) {
            highlightWinner(a, b, c);
            return true;
        }
        return false;
    }

    private void highlightWinner(JButton... tiles) {
        for (JButton tile : tiles) {
            tile.setForeground(Color.green);
            tile.setBackground(Color.gray);
        }
        gameOver = true;
        textLabel.setText(currentPlayer + " wins!");
        textLabel.setForeground(Color.green);
    }

    private void resetGame() {
        // Reset the board
        for (JButton[] row : board) {
            for (JButton tile : row) {
                tile.setText("");
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
            }
        }

        // Reset game state
        gameOver = false;
        gamePaused = false;
        turns = 0;
        currentPlayer = playerX;

        // Reset text label
        textLabel.setText(currentPlayer + "'s turn");
        textLabel.setForeground(Color.white);  // Reset to default text color

        // Reset the frame title and icon
        frame.setTitle("Tic Tac Toe");  // Reset title
    }


    private void togglePause() {
        gamePaused = !gamePaused;
        textLabel.setText(gamePaused ? "Game Paused" : currentPlayer + "'s turn");
    }
}

