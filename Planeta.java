import processing.core.PVector;
import java.lang.Math;

public class Planeta {
    PVector pos;
    PVector vel;
    int raio;
    float constG = 100;
    int height;
    int width;


    public Planeta(int raio, float posX, float posY, float velX, float velY, int height, int width){
        this.raio = raio;
        this.pos = new PVector(posX, posY);
        this.vel = new PVector(velX, velY);
    }

    public PVector getPos(){
        return this.pos;
    }

    public int getRaio(){
        return this.raio;
    }

    public void update(){
        float dX = width/2 - pos.x;
        float dY = height/2 - pos.y;
        float distQuadrado = dX*dX + dY*dY;
        double distancia = Math.sqrt(distQuadrado);
        float forca = constG / distQuadrado;
        double aceleracaoX = forca * dX / distancia;
        double aceleracaoY = forca * dY / distancia;

        vel.x += aceleracaoX;
        vel.y += aceleracaoY;

        pos.add(vel);
    }
}
