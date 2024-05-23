import processing.core.PVector;

class Jogador{
    private PVector pos; 
    private PVector vel = new PVector(0, 0);
    private float angulo = 0;  
    private float maxCombustivel = 100;
    private float combustivel = maxCombustivel;
    private boolean movimento = false;
    private boolean up = false;
    private boolean left = false;
    private boolean right = false;

    public Jogador(PVector pos){
        this.pos = pos;
    }

    public PVector getPos(){
        return this.pos;
    }

    public float getAngulo(){
        return this.angulo;
    }

    public float getCombustivel(){
        return this.combustivel;
    }

    public void pressDirecoes(String direcao){
        if (direcao == "up") this.up = true;
        if (direcao == "left") this.left = true;
        if (direcao == "right") this.right = true;
    }
    public void releaseDirecoes(String direcao){
        if (direcao == "up") this.up = false;
        if (direcao == "left") this.left = false;
        if (direcao == "right") this.right = false;
    }

    public float consomeCombustivel(float valor){
        this.combustivel -= valor;
        return this.combustivel;
    }

    public void emMovimento(){
        this.movimento = true;
    }

    public void parou(){
        this.movimento = false;
    }

    public void movePlayer(){
        this.pos.add(vel);

        if(up && combustivel>0){
            PVector thrust = PVector.fromAngle(angulo).mult(0.1f);
            vel.add(thrust);
            combustivel -= 0.2;
            movimento = true;
        }
        if(left && combustivel>0){
            angulo -= 0.05;
            combustivel -= 0.1;
        }
        if(right && combustivel>0){
            angulo += 0.05;
            combustivel -= 0.1;
        }

         //desacelera o jogador
        if (!movimento) {
            vel.mult(0.99f);
        }

        movimento = false;


    }

    //pos = new PVector(width / 2, height / 2);     iniciar a posição e velocidade?

    /*
    void keyReleased() {
        if (key == CODED) {
            if (keyCode==UP)
                up=false;
                movimento=false;
            if (keyCode==LEFT){
                left=false;
            }
            if (keyCode==RIGHT)
                right=false;
        }
    }

    // Movimenta o jogador 
    void keyPressed() {
        if (key == CODED) {
            if (keyCode == LEFT) {
                //angulo -= 0.05;
                left=true;
            } else if (keyCode == RIGHT) {
                //angulo += 0.05;
                right=true;
            } else if (keyCode == UP) {
                up=true;
                
            }
        }
    }*/
}
