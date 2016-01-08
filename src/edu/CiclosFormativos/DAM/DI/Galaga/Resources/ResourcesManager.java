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
     * Constructor
     * @param input URI del fichero XML
     */
    public ResourcesManager(String input) 
    {
        if (input.isEmpty())
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
    
    /***
     * Carga recursos desde una nodo raíz del XML.
     * @param nodeList Elemento XML a leer
     * @param section Sección en la que se encuentra
     */
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
    }
    
    /***
     * Registra funciones de carga de tipos de recurso par poder ser utilizadas cuando sea necesario cargar un recurso
     * @param resourceType Nombre del tipo de recurso al que se le asocia la función de carga. Hace las funciones de key
     * @param f Función de carga
     */
    public void RegisterLoadFunction(String resourceType, Function<Element,Object> f)
    {
        _loadFuncMap.put(resourceType, f);
    }
    
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
