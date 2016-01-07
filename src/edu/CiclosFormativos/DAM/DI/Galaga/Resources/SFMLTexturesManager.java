/*
 * To change this license header, choose License Headers in Project Properties.
 */
package edu.CiclosFormativos.DAM.DI.Galaga.Resources;

import java.nio.file.*;
import java.io.*;

import org.w3c.dom.Element;

import org.jsfml.graphics.*;

/**
 * Gestiona la carga de recursos de SFML
 * Todos sus métodos estáticos, luego no permite su instancianción
 * (en C# es posible indicar que la clase es static, en Java no)
 * @author aoltra
 */
public class SFMLTexturesManager  {

    /***
     * Carga una Texture desde el disco
     * @param element element xml con la información para la carga
     * @return La Texture leida o null si ha habido problemas
     */
    public static Texture Load(Element element)
    {
        Texture txt = new Texture();
        
        try {
            String path = element.getAttribute("src");
            txt.loadFromFile(Paths.get(path));
        }
        catch (FileSystemNotFoundException | SecurityException | 
                IllegalArgumentException | IOException | NullPointerException ex) {
            return null;
        }  
        
        return txt;    
    }
}
