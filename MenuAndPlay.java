import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;


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
    boolean loggedIn = false;

    Button playButao = new Button(opWidth/2, opHeight/3+opHeight/11, 300, 100, "PLAY");
    Button exitButao = new Button(opWidth/2, opHeight/2+opHeight/11, 300, 100, "EXIT");
    Button loginButton = new Button(opWidth-opWidth/8, opHeight/16+opHeight/11, 200, 80, "Login");

    Button backButton = new Button(opWidth/8, opHeight/16+opHeight/11, 200, 80, "Return");
    Button registerButton = new Button(opWidth-opWidth/8, opHeight/16+opHeight/11, 200, 80, "Register");
    Button enterLogin = new Button(opWidth/2, opHeight-opHeight/6, 200, 80, "Login");
    Button loginUserBox = new Button(opWidth/2, opHeight/3+opHeight/11, 300, 100, "Insira Username");
    Button loginPassBox = new Button(opWidth/2, opHeight/2+opHeight/11, 300, 100, "Insira Password");

    Button enterRegister = new Button(opWidth/2, opHeight-opHeight/6, 200, 80, "Register");

    Jogador jogador = new Jogador(width/2, height/2);

    Planeta planeta = new Planeta(20, width/2, height/2, 1f, 1f, opHeight, opWidth);

    ConnectionManager connectionManager;

    public MenuAndPlay(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    public void settings(){
		size(opWidth, opHeight);
	}

    public void draw(){
        switch (currState) {
            case Menu:
                drawMenuInicial();
                break;
            case Play:
                drawPlay();
                break;
            case Login:
                drawLogin();
                break;
            case Register:
                drawRegister();
                break;
            default:
                break;
        }
    }

    public void mousePressed(){
        switch (currState) {
            case Menu:
                if(loginButton.isOver(mouseX, mouseY)){
                    currState = State.Login;
                } else if(exitButao.isOver(mouseX, mouseY)){
                    exit();
                } else if(playButao.isOver(mouseX, mouseY)){
                    currState = State.Play;
                }
                break;
            case Login:
                if(backButton.isOver(mouseX, mouseY)){
                    loginUserBox.text = "";
                    loginPassBox.text = "";
                    currState = State.Menu;
                }
                if(registerButton.isOver(mouseX, mouseY)){
                    loginUserBox.text = "";
                    loginPassBox.text = "";
                    currState = State.Register;
                }
                if(loginUserBox.isOver(mouseX, mouseY)){
                    loginUserBox.focus();
                    loginUserBox.text = "";
                    username = "";
                }
                if(loginPassBox.isOver(mouseX, mouseY)){
                    loginPassBox.focus();
                    loginPassBox.text = "";
                    password = "";
                }
                if(!loginUserBox.isOver(mouseX, mouseY)){
                    loginUserBox.unfocus();
                    if (loginUserBox.text == ""){
                        loginUserBox.text = "Insira Username";
                    }
                    loginUserBox.showText();
                }
                if(!loginPassBox.isOver(mouseX, mouseY)){
                    loginPassBox.unfocus();
                    if (loginPassBox.text == ""){
                        loginPassBox.text = "Insira Password";
                    }
                    loginPassBox.showText();
                }
                if(enterLogin.isOver(mouseX, mouseY)){
                    println("username: "+ "\""+username+"\"" + " password: "+ "\"" +password+"\"");
                    String response = sendMSG("login/"+username+"/"+password);

                    if(response.equals("ok")){
                        println("recebi ok");
                        currState = State.Menu;
                    } else println(response + " dumbass");
                }
                break;
            case Play:
                break;
            case Register:
                if(backButton.isOver(mouseX, mouseY)){
                    loginUserBox.text = "";
                    loginPassBox.text = "";
                    currState = State.Login;
                }
                if(loginUserBox.isOver(mouseX, mouseY)){
                    loginUserBox.focus();
                    loginUserBox.text = "";
                    username = "";
                }
                if(loginPassBox.isOver(mouseX, mouseY)){
                    loginPassBox.focus();
                    loginPassBox.text = "";
                    password = "";
                }
                if(!loginUserBox.isOver(mouseX, mouseY)){
                    loginUserBox.unfocus();
                    if (loginUserBox.text == ""){
                        loginUserBox.text = "Insira Username";
                    }
                    loginUserBox.showText();
                }
                if(!loginPassBox.isOver(mouseX, mouseY)){
                    loginPassBox.unfocus();
                    if (loginPassBox.text == ""){
                        loginPassBox.text = "Insira Password";
                    }
                    loginPassBox.showText();
                }
                if(enterRegister.isOver(mouseX, mouseY)){
                    println("username: "+ "\""+username+"\"" + " password: "+ "\"" +password+"\"");
                    String response = sendMSG("criarConta/"+username+"/"+password);

                    if(response.equals("ok")){
                        println("recebi ok");
                        currState = State.Menu;
                    } else println(response + " dumbass");
                }
                break;
            default:
                break;
        }
	}

    public void keyPressed(){
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
        switch (currState) {
            case Login:
                if(key == ENTER){
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
            case Register:
                if(key == ENTER){
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
                        jogador.pressDirecoes("left");
                    } else if (keyCode == RIGHT) {
                        jogador.pressDirecoes("right");
                    } else if (keyCode == UP) {
                        jogador.pressDirecoes("up");
                        println(jogador.getPos());                  //debug posição
                    }
                }
                break;
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

    private void drawMenuInicial(){
        background(255);
        displayButton(playButao);
        displayButton(exitButao);
        displayButton(loginButton);
    }

    private void drawRegister(){
        background(120);
        displayButton(loginUserBox);
        displayButton(loginPassBox);
        displayButton(backButton);
        displayButton(enterRegister);
        displayButton(registerButton);
    }

    private void drawLogin(){
        background(120);
        displayButton(loginUserBox);
        displayButton(loginPassBox);
        displayButton(backButton);
        displayButton(enterLogin);
        displayButton(registerButton);
    }

    private void drawPlay(){
        background(60);
        displayJogador(jogador);
        displayPlanetas(planeta);
    }

    private String sendMSG(String msg){
        try {
            println("sending " +msg);
            connectionManager.send(msg);
            println("sent, now receiving");
            String response = connectionManager.receive();
            println("received " + response +", returning");
            return response;
            
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void displayButton(Button button){
        int[] posAndSize = button.getPosAndSize();
        int color = button.isOver(mouseX, mouseY) ? 30: button.getColor();
        fill(color);
        rectMode(CENTER);
        rect(posAndSize[0], posAndSize[1], posAndSize[2], posAndSize[3]);

        if (button.getTextShowing()){
            fill(255);
            textSize(32);
            textAlign(CENTER, CENTER);
            text(button.text, posAndSize[0], posAndSize[1]);
        }
    }

    public void displayJogador(Jogador jogador){
        int raioJogador = 20;
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

    public void displayPlanetas(Planeta planeta){
        PVector pos = planeta.getPos();
        //int raio = planeta.getRaio();

        pushMatrix();
        translate(pos.x, pos.y);
        fill(200);
        ellipse(0, 0, 20*2, 20*2);
        stroke(255, 0, 0);
        popMatrix();

        planeta.update();
    }

    public void run(){
		String[] argum = {"MenuAndPlay"};
		MenuAndPlay  mp = new MenuAndPlay(connectionManager);
		PApplet.runSketch(argum, mp);
	}

}