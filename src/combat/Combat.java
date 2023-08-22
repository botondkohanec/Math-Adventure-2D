package combat;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static main.GamePanel.*;

public abstract class Combat implements Runnable {

    JFrame window;
    ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("player_down_1.png"));
    Container container;
    JPanel panel, inputPanel;
    public JLabel enemyHPLabel, playerHPLabel, questionLabel, solutionLabel,
            bGLabel, enemyAttackLabel, playerAttackLabel, countdownLabel;
    JTextField textField;
    JButton enterB;
    int answerInt = 0;
    public Font font = new Font("Times New Roman", Font.PLAIN, 35);

    InputHandler inputHandler;

    Thread gameThread;

    public boolean next = false;
    String endText = "";

    Random random = new Random();

    int enemyAttack = 0;
    public static int enemyHP;


    public static int NPCcombatIndex;
    public static boolean playerVictory = true;
    public static boolean perfect = true;

    protected Combat() {

        Combat.perfect = true;

        createMainField();
        createPanel();
        createObject();
        createBackGround();
        createInputPanel();

        window.pack();
        window.setVisible(true);

        GamePanel.gameState = GamePanel.playState;
    }

    public void createMainField() {

        window = new JFrame("Combat");
        window.setPreferredSize(new Dimension(800,600));
        window.setSize(800, 600);
        window.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        window.getContentPane().setBackground(Color.black);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setIconImage(icon.getImage());
        container = window.getContentPane();
    }

    public void createPanel() {

        panel = new JPanel();
        panel.setBounds(50, 50, 700, 350);
        panel.setBackground(Color.blue);
        panel.setLayout(null);
        container.add(panel);
    }

    public abstract void createObject();

    public void addKnight() {

        JLabel objectLabel;
        ImageIcon objectIcon;
        objectLabel = new JLabel();
        objectLabel.setBounds(500, 50, 200, 300);

        objectIcon = new ImageIcon(getClass().getClassLoader().getResource("knight01.png"));
        objectLabel.setIcon(objectIcon);

        panel.add(objectLabel);
    }

    public void createBackGround() {

        bGLabel = new JLabel();
        bGLabel.setBounds(0,0,700,350);
        ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("forest01.jpg"));
        bGLabel.setIcon(bgIcon);
        panel.add(bGLabel);
    }

    public void createInputPanel() {

        inputPanel = new JPanel();
        inputPanel.setBounds(50, 460, 700, 50);
        inputPanel.setBackground(Color.black);
        inputPanel.setLayout(new GridLayout(1, 2));

        textField = new JTextField();
        textField.setFont(font);
        inputHandler = new InputHandler(textField, next);
        inputPanel.add(textField);

        enterB = new JButton("Enter");
        enterB.setMnemonic(KeyEvent.VK_ENTER);
        enterB.setForeground(Color.black);
        enterB.addActionListener(inputHandler);
        textField.addKeyListener(inputHandler);
        inputPanel.add(enterB);
        container.add(inputPanel);
    }

    public abstract String play();

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void showQuestion(String question) {

        if(questionLabel != null) questionLabel.setVisible(false);
        questionLabel = new JLabel(question);
        questionLabel.setBackground(Color.getHSBColor(50,200,200));
        questionLabel.setForeground(Color.black);
        questionLabel.setOpaque(true);
        questionLabel.setBounds(50, 410, 700, 50);
        questionLabel.setFont(font);
        container.add(questionLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }
    public void showSolution(String answer, int solution) {
        String text = "";

        answer = answer.replaceAll(" ", "");
        answer = answer.replaceAll("\\+", "");
        if(solutionLabel != null) solutionLabel.setVisible(false);
        switch (GamePanel.language) {

            case GamePanel.eng: text = "Wrong answer! The correct answer is: ";
                break;
            case GamePanel.hun: text = "Nem talált! A helyes válasz: ";
                break;
            case GamePanel.fr: text = "Mauvaise réponse! La bonne solution est: ";
                break;
            default: text = "Wrong answer! The correct answer is: ";
                break;
        }
        solutionLabel = new JLabel(text);
        solutionLabel.setForeground(Color.black);
        solutionLabel.setBackground(Color.white);
        try {
            answerInt = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            answerInt = 0;
        }
        if(answerInt == solution) {
            playSE(8);
            switch (GamePanel.language) {

                case GamePanel.eng: text = "Correct!";
                    break;
                case GamePanel.hun: text = "Talált!";
                    break;
                case GamePanel.fr: text = "Correct!";
                    break;
                default: text = "Correct!";
                    break;
            }
            solutionLabel = new JLabel(text);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.green);
            entity.Player.attack = random.nextInt(10)+1+45;
            showPlayerAttack(entity.Player.attack);
            enemyHP = enemyHP - entity.Player.attack;
            showEnemyHP();
        } else if(entity.Player.hp > 0) {
            Combat.perfect = false;
            playSE(3);
            switch (GamePanel.language) {

                case GamePanel.eng: text = "Wrong answer! The correct answer is: ";
                    break;
                case GamePanel.hun: text = "Nem talált! A helyes válasz: ";
                    break;
                case GamePanel.fr: text = "Mauvaise réponse! La bonne solution est: ";
                    break;
                default: text = "Wrong answer! The correct answer is: ";
                    break;
            }
            solutionLabel = new JLabel(text + solution);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.red);
            if(difficulty == hard) enemyAttack = 20 + random.nextInt(10);
            else if(difficulty == mathematician) enemyAttack = 1000;
            else enemyAttack = random.nextInt(5);
            showEnemyAttack(enemyAttack);
            entity.Player.hp = entity.Player.hp - enemyAttack;
        } else if(entity.Player.hp <= 0) {
            solutionLabel = new JLabel(":(");
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.red);
        }
        solutionLabel.setOpaque(true);
        solutionLabel.setBounds(50, 510, 700, 50);
        solutionLabel.setFont(font);
        container.add(solutionLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }
    public boolean finale() { return true; }

    public void showPlayerHP() {

        String text = "";
        if(playerHPLabel != null) playerHPLabel.setVisible(false);
        switch (GamePanel.language) {

            case GamePanel.eng: text = "Player: ";
                break;
            case GamePanel.hun: text = "Játékos: ";
                break;
            case GamePanel.fr: text = "Joueur: ";
                break;
            default: text = "Player: ";
                break;
        }
        playerHPLabel = new JLabel( text + entity.Player.hp);
        playerHPLabel.setBackground(Color.gray);
        playerHPLabel.setForeground(Color.red);
        playerHPLabel.setBounds(540, 0, 300, 50);
        playerHPLabel.setFont(font);
        container.add(playerHPLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public void showEnemyHP() {}

    public void showPlayerAttack(int attack) {

        if(playerAttackLabel != null) playerAttackLabel.setVisible(false);
        playerAttackLabel = new JLabel( "-" + attack);
        playerAttackLabel.setBackground(Color.gray);
        playerAttackLabel.setForeground(Color.red);
        playerAttackLabel.setBounds(200, 40, 100, 50);
        playerAttackLabel.setFont(font);
        bGLabel.add(playerAttackLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public void showEnemyAttack(int attack)  {

        if(enemyAttackLabel != null) enemyAttackLabel.setVisible(false);
        enemyAttackLabel = new JLabel( "-" + attack);
        enemyAttackLabel.setBackground(Color.gray);
        enemyAttackLabel.setForeground(Color.red);
        enemyAttackLabel.setBounds(490, 40, 100, 50);
        enemyAttackLabel.setFont(font);
        bGLabel.add(enemyAttackLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public void showCountDown(int time) {

        if(countdownLabel != null) countdownLabel.setVisible(false);
        countdownLabel = new JLabel( time+"");
        countdownLabel.setBackground(Color.gray);
        countdownLabel.setForeground(Color.white);
        countdownLabel.setBounds(0, 0, 50, 50);
        countdownLabel.setFont(font);
        bGLabel.add(countdownLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

}