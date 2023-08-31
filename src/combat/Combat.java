package combat;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import static main.GamePanel.*;
import static main.Main.window;

public abstract class Combat implements Runnable {


    GamePanel gp;
//    JFrame window;
    ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("player_down_1.png"));
    Container containerB;
    JPanel panel, inputPanel;
    public JLabel enemyHPLabel, playerHPLabel, questionLabel, solutionLabel,
            bGLabel, countdownLabel;
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

    protected Combat(GamePanel gp) {

        this.gp = gp;
        gp.setVisible(false);
        Combat.perfect = true;


        createMainField();
        createPanel();
        createObject();
        createBackGround();
        createInputPanel();

        window.pack();
        window.setVisible(true);

        GamePanel.gameState = GameState.PLAY_STATE;
    }

    public void createMainField() {

        window.setPreferredSize(new Dimension(gp.screenWith2,gp.screenHeight2));
        window.setSize(gp.screenWith2,gp.screenHeight2);
//        window = new JFrame("Combat");
//        window.setSize(gp.screenWith,gp.screenHeight);
//        window.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        window.getContentPane().setBackground(Color.black);
        // GET LOCAL SCREEN DEVICE

        window.setLocationRelativeTo(null);

//        window.setIconImage(icon.getImage());
        containerB = window.getContentPane();

        containerB.setLayout(null);
    }

    public void createPanel() {

        panel = new JPanel();
        panel.setBounds(100, 50, gp.screenWith2-200, gp.screenHeight2/2+50);
        panel.setBackground(Color.blue);
        panel.setLayout(null);
        containerB.add(panel);
    }

    public abstract void createObject();

    public void addKnight() {

        JLabel objectLabel;
        ImageIcon objectIcon;
        objectLabel = new JLabel();
        objectLabel.setBounds(gp.screenWith2-400, 100, 200, 300);

        objectIcon = new ImageIcon(getClass().getClassLoader().getResource("knight01.png"));
        objectLabel.setIcon(objectIcon);

        panel.add(objectLabel);
    }

    public void createBackGround() {

        bGLabel = new JLabel();
        bGLabel.setBounds(0,0,1200,450);
        ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("forest02.jpg"));
        bGLabel.setIcon(bgIcon);
        panel.add(bGLabel);
    }

    public void createInputPanel() {

        inputPanel = new JPanel();
        inputPanel.setBounds(100, gp.screenHeight2/2+150, gp.screenWith2-200, 50);
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
        containerB.add(inputPanel);
        textField.requestFocus();
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
        questionLabel.setBounds(100, gp.screenHeight2/2+100, gp.screenWith2-200, 50);
        questionLabel.setFont(font);
        containerB.add(questionLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }
    public void showSolution(String answer, int solution) {
        String text = "";

        answer = answer.replaceAll(" ", "");
        answer = answer.replaceAll("\\+", "");
        if(solutionLabel != null) solutionLabel.setVisible(false);
        text = GamePanel.switchLanguage("Wrong answer! The correct answer is: ",
                "Nem talált! A helyes válasz: ",
                "Mauvaise réponse! La bonne solution est: ");
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
            text = GamePanel.switchLanguage("Correct!", "Talált!", "Correct!");
            solutionLabel = new JLabel(text);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.green);
            entity.Player.attack = random.nextInt(10)+1+45;
            enemyHP = enemyHP - entity.Player.attack;
            showEnemyHP();
        } else if(entity.Player.hp > 0) {
            Combat.perfect = false;
            playSE(3);
            text = GamePanel.switchLanguage("Wrong answer! The correct answer is: ",
                    "Nem talált! A helyes válasz: ",
                    "Mauvaise réponse! La bonne solution est: ");
            solutionLabel = new JLabel(text + solution);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.red);
            if(GamePanel.difficulty == Difficulty.HARD) enemyAttack = 20 + random.nextInt(10);
            else if(GamePanel.difficulty == Difficulty.MATHEMATICIAN) enemyAttack = 1000;
            else enemyAttack = random.nextInt(5);
            entity.Player.hp = entity.Player.hp - enemyAttack;
        } else if(entity.Player.hp <= 0) {
            solutionLabel = new JLabel(":(");
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.red);
        }
        solutionLabel.setOpaque(true);
        solutionLabel.setBounds(100, gp.screenHeight2/2+200, gp.screenWith2-200, 50);
        solutionLabel.setFont(font);
        containerB.add(solutionLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }
    public boolean finale() { return true; }

    public void showPlayerHP() {

        String text = "";
        if(playerHPLabel != null) playerHPLabel.setVisible(false);
        text = GamePanel.switchLanguage("Player: ", "Játékos: ", "Joueur: ");
        playerHPLabel = new JLabel( text + entity.Player.hp);
        playerHPLabel.setBackground(Color.gray);
        playerHPLabel.setForeground(Color.red);
        playerHPLabel.setBounds(gp.screenWith2-300, 0, 300, 50);
        playerHPLabel.setFont(font);
        containerB.add(playerHPLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public void showEnemyHP() {}
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