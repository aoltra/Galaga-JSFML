/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.CiclosFormativos.DAM.DI.Galaga;

import org.jsfml.window.event.Event;
import org.jsfml.window.*;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;


/**
 *
 * @author aoltra
 */
public class Game {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();             // instanciación del juego 
	game.run();                         // llamada a la función de arranque
    }
    
    // Variables miembro
    private RenderWindow _window = null;                    // ventana principal
	
    private CircleShape _player = null;                     // jugador (un simple círculo cyan)
    private Boolean _IsMovingUp = false, _IsMovingDown = false, _IsMovingLeft = false, _IsMovingRight = false;
    private float _playerSpeed;                             // velocidad del jugad
    
    // Constructor
    public Game() {

        // buffer 32 bits de colors, version OpenGL 2.0
        ContextSettings contextSettings = new ContextSettings(32,0,0,2,0);

        // Creamos la ventana principal
        _window = new RenderWindow();
        _window.create(new VideoMode(1280, 1024), "Galaga", WindowStyle.DEFAULT, contextSettings);

        _player = new CircleShape ();
        _player.setRadius(40f);
        _player.setPosition(new Vector2f(100f, 100f));
        _player.setFillColor(Color.CYAN);
        
        _playerSpeed = 25;           // 25 px/s
    }
    
    ////////////////////////
    // Métodos
    ////////////////////////
    public void run() {

        org.jsfml.system.Clock clock = new org.jsfml.system.Clock();
        org.jsfml.system.Time deltaTime;
        
        // Game Loop
        while (_window.isOpen())
        {
            // para cada uno de los ciclos reinicio el reloj a cero y devuelvo
           // el tiempo que ha pasado desde el inicio
           deltaTime = clock.restart();

            // Procesamos eventos
            dispacthEvent();
            
            update(deltaTime);
            render();
        }
    }
    
    // Despacha los eventos
    public void dispacthEvent() {
    
        Event event;
        
        while ((event = _window.pollEvent()) != null) {
            
            if (event.type == Event.Type.CLOSED)
                _window.close();
            
            if (event.type == Event.Type.KEY_PRESSED)
                handlePlayerInput(event.asKeyEvent().key, true);
            
            if (event.type == Event.Type.KEY_RELEASED)
                handlePlayerInput(event.asKeyEvent().key, false);
         
        }
    }
    
    // actualiza el estado del mundo
    private void update(org.jsfml.system.Time time) {
        
        float xPos = _player.getPosition().x;
        float yPos = _player.getPosition().y;
        
        float dist = _playerSpeed * time.asSeconds();
        
        if (_IsMovingUp)
            yPos -= dist;
        if (_IsMovingDown)
            yPos += dist;
        if (_IsMovingLeft)
            xPos -= dist;
        if (_IsMovingRight)
            xPos += dist;
   
        // espacio = velocidad * tiempo. El nuevo espacio se añade a la posición previa
        // del tiempo se obtienen los segundos ya que la velocidad se dam en px/s
        _player.setPosition(xPos, yPos);
    }
		
    // Dibuja el mundo
    private void render() { 
        // limpia la pantalla (por defecto en negro, pero podemos asignarle un color)
        _window.clear();
        // Dibuja un elemento "dibujable", Drawable. En este caso nuestro "jugador": el círculo
        _window.draw(_player);
        // muestra la pantalla
        _window.display();
    }
    
    /////////////////////////////////////
    // Funciones que gestionan eventos
    /////////////////////////////////////
    private void handlePlayerInput(org.jsfml.window.Keyboard.Key key, Boolean pressed) {

        if (key == org.jsfml.window.Keyboard.Key.W)
            _IsMovingUp = pressed;
        else if (key == org.jsfml.window.Keyboard.Key.S)
            _IsMovingDown = pressed;
        else if (key == org.jsfml.window.Keyboard.Key.A)
            _IsMovingLeft = pressed;
        else if (key == org.jsfml.window.Keyboard.Key.D)
            _IsMovingRight = pressed;
    }
	
}