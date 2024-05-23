import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.List;
import java.util.Set;


public class MenuAndPlay extends PApplet implements Runnable{ //implementar runnable

    enum State{
        Menu,
        Play,
        Login,
        Register
    }

    private State currState = State.Menu;
    private int opHeight = 720;
    private int opWidth = 1280;
    String username = "";
    String password = "";
    Button playButao = new Button(opWidth/2, opHeight/3+opHeight/11, 300, 100, "PLAY");
    Button exitButao = new Button(opWidth/2, opHeight/2+opHeight/11, 300, 100, "EXIT");
    Button loginButton = new Button(opWidth/1-opWidth/8, opHeight/16+opHeight/11, 200, 80, "Login");
    Button backButton = new Button(opWidth/1-opWidth/8, opHeight/16+opHeight/11, 200, 80, "Return");
    Button enterLogin = new Button(opWidth/2, opHeight/1-opHeight/6, 200, 80, "Login");

    Button loginUserBox = new Button(opWidth/2, opHeight/3+opHeight/11, 300, 100, "Insira Username");
    Button loginPassBox = new Button(opWidth/2, opHeight/2+opHeight/11, 300, 100, "Insira Password");

    PVector pos = new PVector(width / 2, height / 2);

    private int raioJogador = 20;
    Jogador jogador = new Jogador(pos);

    ConnectionManager connectionManager;

    public MenuAndPlay(/*ConnectionManager connectionManager*/){
        //this.connectionManager = connectionManager;
    }

    public void settings(){
		size(opWidth, opHeight);
	}

    public void draw(){
        switch (currState) {
            case Menu:
                menuInicial();
                break;
            case Play:
                play();
                break;
            case Login:
                login();
                break;
            case Register:
                register();
                break;
            default:
                break;
        }
    }

    public void mousePressed(){
        switch (currState) {
            case Menu:
                if(loginButton.isOver()){
                    currState = State.Login;
                } else if(exitButao.isOver()){
                    exit();
                } else if(playButao.isOver()){
                    currState = State.Play;
                }
                break;
            case Login:
                if(backButton.isOver()){
                    loginUserBox.text = "";
                    loginPassBox.text = "";
                    currState = State.Menu;
                }
                if(loginUserBox.isOver()){
                    loginUserBox.focus();
                    loginUserBox.text = "";
                    username = "";
                }
                if(loginPassBox.isOver()){
                    loginPassBox.focus();
                    loginPassBox.text = "";
                    password = "";
                }
                if(!loginUserBox.isOver()){
                    loginUserBox.unfocus();
                    if (loginUserBox.text == ""){
                        loginUserBox.text = "Insira Username";
                    }
                    loginUserBox.showText();
                }
                if(!loginPassBox.isOver()){
                    loginPassBox.unfocus();
                    if (loginPassBox.text == ""){
                        loginPassBox.text = "Insira Password";
                    }
                    loginPassBox.showText();
                }
                if(enterLogin.isOver()){
                    println("username: "+ "\""+username+"\"" + " password: "+ "\"" +password+"\"");
                    /*String response = loginInfo("login#"+username+"#"+password);
                    if(response == "Ok"){
                        println("recebi ok");
                        //currState = State.Menu;
                        println(response);
                    } else {
                        println(response + " dumbass");
                        exit();
                    }*/

                }
                break;
            case Play:
                play();
                break;
            case Register:
                register();
                break;
            default:
                break;
        }
	}

    public void keyPressed(){
        switch (currState) {
            case Login:
                if(loginUserBox.getfocus()){
                    if(key == BACKSPACE && loginUserBox.text != ""){
                        loginUserBox.text=loginUserBox.text.substring(0, loginUserBox.text.length()-1);
                        username = username.substring(0, username.length()-1);
                    }else if (key != BACKSPACE){
                        loginUserBox.text += key;
                        username += key;
                    }
                }
                else if(loginPassBox.getfocus()){
                    if (key == BACKSPACE && loginPassBox.text != ""){
                        loginPassBox.text=loginPassBox.text.substring(0, loginPassBox.text.length()-1);
                        password = password.substring(0, password.length()-1);
                    } else if (key != BACKSPACE){
                        password += key;
                        loginPassBox.text += '*';
                    }
                }
                if(key == ENTER){
                    println("username: "+ "\""+username+"\"" + " password: "+ "\"" +password+"\"");
                        /*String response = loginInfo("login#"+username+"#"+password);
                        if(response == "Ok"){
                            println("recebi ok");
                            //currState = State.Menu;
                            println(response);
                        } else {
                            println(response + " dumbass");
                            exit();
                        }*/
                }
                break;
                
            case Play:
                if (key == CODED) {
                    if (keyCode == LEFT) {
                        //angulo -= 0.05;
                        jogador.pressDirecoes("left");
                        println(jogador.getPos());
                    } else if (keyCode == RIGHT) {
                        //angulo += 0.05;
                        jogador.pressDirecoes("right");
                    } else if (keyCode == UP) {
                        jogador.pressDirecoes("up");
                        println(jogador.getPos());
                    }
                }
            default:
                break;
        }
    }

    public void keyReleased(){
        switch (currState) {
            case Play:
                if (key == CODED) {
                    if (keyCode == LEFT) {
                        jogador.releaseDirecoes("left");
                    } else if (keyCode == RIGHT) {
                        jogador.releaseDirecoes("right");
                    } else if (keyCode == UP) {
                        jogador.releaseDirecoes("up");
                    }
                }
                break;
        
            default:
                break;
        }
    }

    private void menuInicial(){
        background(255);
        playButao.display();
        exitButao.display();
        loginButton.display();
        exitButao.isOver();
        playButao.isOver();
        loginButton.isOver();
    }

    private void register(){
        playButao.display();
        exitButao.display();
        exitButao.isOver();
        playButao.isOver();
    }

    private void login(){
        background(80);
        loginUserBox.display();
        loginPassBox.display();
        backButton.display();
        enterLogin.display();
        loginUserBox.isOver();
        loginPassBox.isOver();
        backButton.isOver();
        enterLogin.isOver();
    }

    private void play(){
        background(60);
        displayJogador(jogador);
    }

    private String loginInfo(String userInfo){
        try {
            connectionManager.send(userInfo);
            String response = connectionManager.receive();
            return response;
            
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    class Button{
        private int x;
        private int y;
        private int sizeX;
        private int sizeY;
        private int color = 60;
        private boolean textShowing = true;
        private boolean focus = false;
        public String text;

        public Button(int x, int y, int sizeX, int sizeY, String text){
            this.x = x;
            this.y = y;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.text = text;
            this.color = 60;
        }

        public Boolean isOver(){
            if (mouseX >= this.x-(this.sizeX/2) && mouseX <= this.x+(this.sizeX/2) &&
                 mouseY >= this.y-(this.sizeY/2) && mouseY <= this.y+(this.sizeY/2)) {
                this.color = 0;
                return true;
            } else {
                this.color = 60;
                return false;
            }
        }

        public void display(){
            fill(this.color);
            rectMode(CENTER);
            rect(this.x, this.y, this.sizeX, this.sizeY);

            if (this.textShowing){
                fill(255);
                textSize(32);
                textAlign(CENTER, CENTER);
                text(this.text, this.x, this.y);
            }
        }

        public void showText(){
            this.textShowing = true;
        }

        public void hideText(){
            this.textShowing = false;
        }

        public boolean getfocus(){
            return this.focus;
        }

        public void focus(){
            this.focus = true;
        }

        public void unfocus(){
            this.focus = false;
        }
    }

    public void displayJogador(Jogador jogador){
        float combustivel = jogador.getCombustivel();
        float angulo = jogador.getAngulo();
        PVector pos = jogador.getPos();

        if(pos.x > width + raioJogador || pos.x < 0 - raioJogador ||
             pos.y > height + raioJogador || pos.y < 0 - raioJogador) exit();

        pushMatrix();
        translate(pos.x, pos.y);
        rotate(angulo);
        fill(255);
        ellipse(0, 0, raioJogador*2, raioJogador*2);
        stroke(255, 0, 0);
        line(0, 0, 20, 0); //linha da direção
        popMatrix();

        fill(255);
        textSize(16);
        text("Combustivel: " + (int)(combustivel), width/11, height/12);

        jogador.movePlayer();
    }

    public void run(){
		String[] argum = {"MenuAndPlay"};
		MenuAndPlay  mp = new MenuAndPlay();
		PApplet.runSketch(argum, mp);
	}

}