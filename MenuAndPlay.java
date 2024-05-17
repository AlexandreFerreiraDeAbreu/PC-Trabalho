import processing.core.PApplet;
import java.util.List;
import java.util.Set;


public class MenuAndPlay extends PApplet { //implementar runnable

    private int currState = 0;
    private int opHeight = 720;
    private int opWidth = 1280;
    private int background = 80;
    Button playButao = new Button(opWidth/2, opHeight/3+opHeight/11, 300, 100, "PLAY");
    Button exitButao = new Button(opWidth/2, opHeight/2+opHeight/11, 300, 100, "EXIT");

    public MenuAndPlay(){

    }

    public void settings(){
		size(opWidth, opHeight);
	}

    public void draw(){
        switch (currState) {
            case 0:
                menuInicial();
                break;
            case 1:
                play();
                break;
            default:
                break;
        }
    }

    public void mousePressed(){
		if(playButao.isOver()){
            currState = 1;
        } else if(exitButao.isOver()){
            exit();
        }
	}
    
    private void menuInicial(){
        playButao.display();
        exitButao.display();
        exitButao.isOver();
        playButao.isOver();
    }

    private void play(){
        background(60);
        ellipse(mouseX, mouseY, 50, 50);
    }

    public static void main(String[] args){
		String[] argum = {"MenuAndPlay"};
		MenuAndPlay  mp = new MenuAndPlay();
		PApplet.runSketch(argum, mp);
	}

    class Button{
        private int x;
        private int y;
        private int sizeX;
        private int sizeY;
        private String text;
        private int color = 60;

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
            fill(255);
            textSize(32);
            textAlign(CENTER, CENTER);
            text(this.text, this.x, this.y);
        }

    }

}