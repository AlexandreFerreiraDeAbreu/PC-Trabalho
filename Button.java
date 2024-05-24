public class Button{
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
    }

    public Boolean isOver(int mouseX, int mouseY){
        if (mouseX >= this.x-(this.sizeX/2) && mouseX <= this.x+(this.sizeX/2) &&
             mouseY >= this.y-(this.sizeY/2) && mouseY <= this.y+(this.sizeY/2)) {
            this.color = 0;
            return true;
        } else {
            this.color = 60;
            return false;
        }
    }

    public int getColor(){
        return this.color;
    }

    public int[] getPosAndSize(){
        int[] posAndSize = {this.x, this.y, this.sizeX, this.sizeY};
        return posAndSize;
    }

    public boolean getTextShowing(){
        return this.textShowing;
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