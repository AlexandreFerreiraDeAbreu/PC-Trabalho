import processing.core.PVector;
import java.lang.Math;

public class Planeta {
    PVector pos;
    PVector vel;
    int raio;
    float constG = 100;
    int height;
    int width;


    public Planeta(int raio, float posX, float posY, float velX, float velY, int width, int height){
        this.raio = raio;
        this.pos = new PVector(posX, posY);
        this.vel = new PVector(velX, velY);
        this.width = width;
        this.height = height;
    }

    public PVector getPos(){
        return this.pos;
    }

    public int getRaio(){
        return this.raio;
    }

    public void update(){
        float dX = this.width/2 - pos.x;
        float dY = this.height/2 - pos.y;
        float distQuadrado = dX*dX + dY*dY;
        double distancia = Math.sqrt(distQuadrado);
        float forca = constG / distQuadrado;
        if (forca > 2) forca = 2;
        if (forca < -2) forca = -2;
        double aceleracaoX = forca * dX / distancia;
        double aceleracaoY = forca * dY / distancia;

        vel.x += aceleracaoX;
        vel.y += aceleracaoY;
        
        //System.out.println(forca);

        pos.add(vel);
    }
}
