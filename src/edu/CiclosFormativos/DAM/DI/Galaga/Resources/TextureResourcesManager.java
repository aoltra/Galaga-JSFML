/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.CiclosFormativos.DAM.DI.Galaga.Resources;

import java.util.Map;
import java.lang.ref.WeakReference; 
import java.util.HashMap; 
import java.nio.file.*;
import java.io.*;

import org.jsfml.graphics.*;

/**
 *  Gestor de Recursos de tipo textura
 * @author aoltra
 */
public class TextureResourcesManager {
    
    // los recursos serán almacenados para su gestión en un mapa
    // En Java podemos optar por WeakReference o por SoftReference. Esta última se comporta de tal 
    // manera que sólo serán recoletadas por el GC si el sistema necesita memoria (están más enfocadas a 
    // la creación cachés, mientras que las WeakReferences lo están a la creación de contenedores de referencias
    // justo lo que necesitamos. 
    Map<Integer,WeakReference> _textureMap = new HashMap<>();
    
    /***
     * Carga una textura desde el sistema de archivos
     * @param id Identificador a asignar a la textura cargada
     * @param filename Nombre del fichero donde está ubicada la textura 
     * @throws java.lang.Exception 
     */    
    public void load(int id, String filename) throws Exception {
            
        // Referencia débil. En cuanto la última referencia fuerte al objeto desaparezca, el GC estará
        // en disposición de recolectarlo
        try {
             Texture txt = new Texture();
             txt.loadFromFile(Paths.get(filename));
             WeakReference<Texture> wr = new WeakReference<>(txt);

             _textureMap.put(id, wr);
        }
        catch (FileSystemNotFoundException | SecurityException | 
                IllegalArgumentException | IOException | NullPointerException ex) {
            throw new Exception("Excepcion al cargar " +  filename + ". " + ex.getMessage());
        }  
    }
    
    /***
     * Devuelve una textura en función de su id
     * @param id identificador de la textura que buscamos
     * @return textura almacenada con id 
     */
    public Texture getTexture(int id) {
        
        WeakReference wr;
        
        // compruebo si existe el id
        if (_textureMap.containsKey(id))
        {
            wr = _textureMap.get(id);
            
            // si existe compruebo si no ha sido eliminado por el GC
            if (!wr.isEnqueued()) return (Texture)wr.get();
            
            // si ha sido eliminado... elimino la key
            _textureMap.remove(id);
        }
        
        return null;
    }
}
