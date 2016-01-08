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
public class SFMLResourcesManager  {

    /***
     * Carga una Texture desde el disco
     * @param element element xml con la información para la carga
     * @return La Texture leida o null si ha habido problemas
     */
    public static Texture LoadTexture(Element element)
    {
        Texture txt = new Texture();
        
        try {
            String path = element.getAttribute("src");
            if (path == null) return null;
             
            // Tamaño
            String rect = element.getAttribute("rectangle");
            if (!rect.isEmpty())
            {
                String[] rectCoord;
                rectCoord = rect.split(",");
                IntRect area = new IntRect(Integer.parseInt(rectCoord[0]), Integer.parseInt(rectCoord[1]),Integer.parseInt(rectCoord[2]),Integer.parseInt(rectCoord[1])); 
                txt.loadFromFile(Paths.get(path),area);
            } else txt.loadFromFile(Paths.get(path));
            
            // Propiedades
            txt.setRepeated(Boolean.parseBoolean(element.getAttribute("repeated")));
            txt.setSmooth(Boolean.parseBoolean(element.getAttribute("smooth")));
        }
        catch (FileSystemNotFoundException | SecurityException | 
                IllegalArgumentException | IOException | NullPointerException ex) {
            return null;
        }  
        
        return txt;    
    }
}
