package hu.lechnerkozpont.validation;

import hu.lechnerkozpont.kata.exception.IllegalParametersException;

public class ArgsValidator {
    public void check(String [] parameters){
        if(parameters == null)
            throw new IllegalParametersException();
    }
}
