package hu.lechnerkozpont.kata;

import exception.InvalidPortNumberException;
import exception.PortValueMissingException;
import exception.UnknownParameterException;

public class ArgsInputCheck {
    private static final String PORT_FLAG = "-p";
    private static final String LOGGING_FLAG = "-l";
    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65536;

    private boolean fileNameParameterIsAlreadyFound=false;
    private boolean logParameterIsAlreadyFound=false;
    private boolean portParameterIsAlreadyFound=false;
    public void check(String[] parameters) {
            int i=0;
            while (i < parameters.length) {
                ifUknownParameterThenThrows(parameters[i],i+1);
                if (("-p").equals(parameters[i]) && !portParameterIsAlreadyFound)
                {
                    if(i+1< parameters.length) {
                        validateParameterAfterPortFlag(parameters[i + 1]);
                        portParameterIsAlreadyFound = true;
                    }
                    else
                        portNumberMissingError();
                    i++;
                }
                else if(("-l").equals(parameters[i])){
                    if(!logParameterIsAlreadyFound)
                        logParameterIsAlreadyFound=true;
                    else {
                        if (fileNameParameterIsAlreadyFound)
                            unknownParameterError(parameters[i], i+1);
                    }
                }
                else if (!("").equals(parameters[i])){
                    if (!fileNameParameterIsAlreadyFound)
                        fileNameParameterIsAlreadyFound = true;
                    else
                        unknownParameterError(parameters[i], i+1);
                }
                i++;
            }
    }
    private void validateParameterAfterPortFlag(String parameter){
        if(!isInteger(parameter))
            portNumberMissingError();
        validatePortNumber(parameter);
    }
    private void portNumberMissingError() {
        throw new PortValueMissingException();
    }
    private void validatePortNumber(String nextParameter) {
        int inputPortNumber = Integer.parseInt(nextParameter);
        if(!isPortNumberBetweenRange(inputPortNumber))
            invalidPortNumberError();
    }
    private void invalidPortNumberError() {
        throw new InvalidPortNumberException();
    }
    private boolean isPortNumberBetweenRange(int inputPortNumber) {
        return inputPortNumber >= MIN_PORT_NUMBER && inputPortNumber <= MAX_PORT_NUMBER;
    }
    private boolean isInteger(String inputString) {
        try {
            Integer.parseInt(inputString);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private boolean isFileNameParameterNotFound() {
        return !fileNameParameterIsAlreadyFound;
    }
    private void ifUknownParameterThenThrows(String parameter, int i) {
        if(!isParameterUseAble(parameter))
            unknownParameterError(parameter, i);
    }
    private boolean isParameterUseAble(String parameter) {
        return isParameterUseableForLogging(parameter) || isParameterUseableForPort(parameter) || isFileNameParameterNotFound();
    }

    private boolean isParameterUseableForPort(String parameter) {
        return isPortParameter(parameter) && !portParameterIsAlreadyFound;
    }

    private boolean isParameterUseableForLogging(String parameter) {
        return isLoggingParameter(parameter) && !logParameterIsAlreadyFound;
    }
    private void unknownParameterError(String parameter, int i) {
        throw new UnknownParameterException(i, parameter);
    }

    private boolean isPortParameter(String parameter) {
        return PORT_FLAG.equals(parameter);
    }
    private boolean isLoggingParameter(String parameter) {
        return LOGGING_FLAG.equals(parameter);
    }
}
