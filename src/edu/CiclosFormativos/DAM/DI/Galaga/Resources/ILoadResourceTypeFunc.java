/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.CiclosFormativos.DAM.DI.Galaga.Resources;

import org.w3c.dom.Element;

/**
 * Interfaz que han de seguir las clases que definen las funciones de carga de tipos de recursos
 * Se declara como interface funcional (Java 8 o superior) de manera que pueda utulizarse 
 * como referencia a método
 * @author aoltra
 */
@FunctionalInterface
public interface ILoadResourceTypeFunc {
    public Object Load(Element e);
}
