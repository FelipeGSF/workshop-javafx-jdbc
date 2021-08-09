/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author felipe
 * 
 * Classe de excessão personalizada que carrega 
 * uma coleção contendo todos os erros possíveis
 * com os nomes dos campos e os erros de cada campo
 */
public class ValidationException extends RuntimeException{
    
    private static final long serialVersionUID = 1L;
    
    private Map<String, String> errors = new HashMap<>();
    
    public ValidationException(String msg){
        super(msg);
    }
    
    public Map<String, String> getErrors(){
        return errors;
    }
    
    // Adiciona um elemento a coleção. Neste caso adiciona um nome de campo e um erro para esse campo.
    public void addError(String fieldName, String errorMessage){
        errors.put(fieldName, errorMessage);
    }
    
}
