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
	
    private CircleShape _player = null;                    // jugador (un simple círculo cyan)
    private Boolean _IsMovingUp = false, _IsMovingDown = false, _IsMovingLeft = false, _IsMovingRight = false;
    
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
    }
    
    ////////////////////////
    // Métodos
    ////////////////////////
    public void run() {

        // Game Loop
        while (_window.isOpen())
        {
            // Procesamos eventos
            dispacthEvent();

            update();
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
    private void update() {
        
        float xPos = _player.getPosition().x;
        float yPos = _player.getPosition().y;
        
        // desplaza 1 px en el sentido que haya inidcado la pulsacion del teclado
        if (_IsMovingUp)
            yPos -= 1f;
        if (_IsMovingDown)
            yPos += 1f;
        if (_IsMovingLeft)
            xPos -= 1f;
        if (_IsMovingRight)
            xPos += 1f;
    
        
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