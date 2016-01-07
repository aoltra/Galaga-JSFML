/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.CiclosFormativos.DAM.DI.Galaga.Resources;

import java.util.Map;
import java.lang.ref.WeakReference; 
import java.util.HashMap; 

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import java.util.function.*;
import java.nio.file.*;
import java.io.*;
import org.xml.sax.SAXException;

/**
 * Gestiona la carga de todo tipo de recursos desde ficheros XML
 * @author aoltra
 */
public class ResourcesManager {

    /***
     * Separador de secciones
     * Se necesita para definir el acceso a un recurso dentro de una sección.
     */    
    public static final String SECTION_SEPARATOR = ":";
    
    
    // los recursos serán almacenados para su gestión en un mapa
    // En Java podemos optar por WeakReference o por SoftReference. Esta última se comporta de tal 
    // manera que sólo serán recoletadas por el GC si el sistema necesita memoria (están más enfocadas a 
    // la creación cachés, mientras que las WeakReferences lo están a la creación de contenedores de referencias
    // justo lo que necesitamos. 
    private Map<String,Resource> _resourcesMap = new HashMap<>();
    // diccionario que almacena la posibles funciones de carga
    private Map<String, Function<Element,Object>> _loadFuncMap = new HashMap<>();

    /***
     * 
     * @param input URI del fichero XML
     */
    public ResourcesManager(String input) 
    {
        if (input == null)
            return;

        try {
            
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            
            if (doc.hasChildNodes()) {
                Load(doc.getElementsByTagName("resx").item(0).getChildNodes(),"");
            }      
        }
        catch (IOException | ParserConfigurationException | SAXException ex)
        {
        
        }
    }
    
     /// <summary>
        /// Carga recursos desde una nodo raíz del XML.
        /// </summary>
        /// <param name="el">Elemento XML a leer</param>
        /// <param name="section">Seccion en la que se encuentra</param>
    private void Load(NodeList nodeList, String section)
    {
        Element element = null;
        String id;
        
        if (!section.isEmpty())
            section += SECTION_SEPARATOR;
        else
            section = "";
        
        // no puedo utilizar un foreach ya que NodeList no es Iterable :(
        for (int count = 0; count < nodeList.getLength(); count++) {

            Node node = nodeList.item(count);
        
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                
                element = (Element)node;
                id = element.getAttribute("id");
                
                if (!id.isEmpty())  // hay atributo id
                {
                    if (node.getNodeName().contentEquals("section"))
                        Load(node.getChildNodes(), id);
                    else 
                        _resourcesMap.put(section + id, new Resource(element));
                }
            }
        }

	// make sure it's element node.
	

//        el.getElements();
//        // repaso todos los elementos del nodo
//        for (Element element: el.getChildNodes())
//        {                
//            // obtengo el valor del atributo id
//            String id = element.getAttribute("id");
//            if (id != null)
//            {
//                    // si es una sección sigo leyendo el árbol.. hasta encontrar un recurso
//                    if (element.Name == "section")
//                        Load(element, id.Value);
//                    else
//                    {
//                      //  WeakReference wr = new WeakReference(new SFML.Graphics.Texture(filename));
//                        _resourcesMap.Add(section + id.Value, new Resource(element));
//                    }
//                }
//        }
    }
    
    
//    
//    /***
//     * Carga una textura desde el sistema de archivos
//     * @param id Identificador a asignar a la textura cargada
//     * @param filename Nombre del fichero donde está ubicada la textura 
//     * @throws java.lang.Exception 
//     */    
//    public void load(int id, String filename) throws Exception {
//            
//        // Referencia débil. En cuanto la última referencia fuerte al objeto desaparezca, el GC estará
//        // en disposición de recolectarlo
//        try {
//             Texture txt = new Texture();
//             txt.loadFromFile(Paths.get(filename));
//             WeakReference<Texture> wr = new WeakReference<>(txt);
//
//             _textureMap.put(id, wr);
//        }
//        catch (FileSystemNotFoundException | SecurityException | 
//                IllegalArgumentException | IOException | NullPointerException ex) {
//            throw new Exception("Excepcion al cargar " +  filename + ". " + ex.getMessage());
//        }  
//    }
    
    /***
     * Devuelve un recurso en función de su id
     * @param id identificador del recurso que buscamos
     * @return recurso almacenado en ese id 
     * @throws java.lang.Exception Salta si no se ha definido una función para la carga del tipo de recurso solicitado
     */
    public Object getID(String id) throws Exception {
        
        Resource res;                               // recurso del mapa de recursos devuelto
        Function<Element,Object> loadFunc;         // funcion de carga elegida para la carga de un tipo concreto de recurso

        // compruebo si existe el id, en caso contrario devolverá null
        if (_resourcesMap.containsKey(id))
        {
            res = _resourcesMap.get(id);
            Object objectResource = res.getWeakRef().get();

            if (objectResource == null) // si es null es que no aun no está cargado o se ha descargado
            {
                // hay que cargarlo
                // obtengo el tipo del recurso a partir del nombre del elemento XElement
                String type = res.getElement().getNodeName();

                if (_loadFuncMap.containsKey(type))
                {
                    loadFunc = _loadFuncMap.get(type);
                    
                    objectResource = loadFunc.apply(res.getElement());                 
                }
                else
                    throw new Exception("ERROR: [GestorRecursos] No se ha definido una función para la carga del tipo de recurso " + 
                                 type +". No se ha cargado el recurso '" + res.getElement() + "'");
            }
            
            return objectResource;
        }
        
        return null;
        
        

//        
//        WeakReference wr;
//        
//        // compruebo si existe el id
//        if (_textureMap.containsKey(id))
//        {
//            wr = _textureMap.get(id);
//            
//            // si existe compruebo si no ha sido eliminado por el GC
//            if (!wr.isEnqueued()) return wr.get();
//            
//            // si ha sido eliminado... elimino la key
//            _textureMap.remove(id);
//        }
//        
//        return null;
    }
    
   
    /***
     *  Encapsula un recurso en el mapa de recursos.
     */
    private class Resource
    {
        private WeakReference weakref; 
        private Element element;

        /***
         * Constructor
         * @param el elemento xml
         */
        public Resource(Element el)
        {
            element = el;
            weakref = new WeakReference(null);
        }

        /***
         * Devuelve el elemento XML con la información necesria para cargar el recurso
         * @return Element XML
         */
        public Element getElement() { return element; }

        /***
         * Devuelve la referencia débil al recurso.
         * @return Referencia débil del recurso
         */
        public WeakReference getWeakRef() { return weakref; }
    }
}
