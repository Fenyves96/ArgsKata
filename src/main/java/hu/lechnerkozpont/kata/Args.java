package hu.lechnerkozpont.kata;

import exception.InvalidPortNumberException;
import exception.PortValueMissingException;
import exception.UnknownParameterException;
import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang.math.NumberUtils.isNumber;

public class Args {
    private static final String PORT_FLAG = "-p";
    private static final String LOGGING_FLAG = "-l";

    private static final String DEFAULT_FILE_NAME = "";
    private static final boolean DEFAULT_NO_LOGGING = false;
    private static final int DEFAULT_PORT_NUMBER = 8080;

    private String fileName = DEFAULT_FILE_NAME;
    private boolean isLogging = DEFAULT_NO_LOGGING;
    private int port = DEFAULT_PORT_NUMBER;
    List<String> parameters = new ArrayList<>();


    public void setParameters(String[] parameters) {
        resetDefaults();
        check(parameters);
        collectUsefulParameters(parameters);
        parseAllParameters();
    }

    private void resetDefaults() {
        parameters = new ArrayList<>();
        setFileName(DEFAULT_FILE_NAME);
        isLogging = DEFAULT_NO_LOGGING;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isLogging() {
        return isLogging;
    }

    private void check(String[] parameters) {
        if(parameters == null)
            throw new IllegalParametersException();
    }

    private void collectUsefulParameters(String[] parameters) {
        for (String parameter: parameters) {
            if(StringUtils.isNotEmpty(parameter))
                this.parameters.add(parameter);
        }
    }

    private void parseAllParameters() {
        int position = 0;
        for (String parameter : parameters) {
            position++;
            ifUknownParameterThenThrows(position, parameter);
            parseOneParameter(parameter);
        }
    }

    private void ifUknownParameterThenThrows(int position, String parameter) {
        if(isAllParameterSetted())
            unknownParameterError(position, parameter);
    }

    private boolean isAllParameterSetted() {
        return isLogging && isFileNameNotDefault();
    }

    private boolean isFileNameNotDefault() {
        return  !fileName.equals(DEFAULT_FILE_NAME);
    }

    private void parseOneParameter(String parameter) {
        if (isLoggingParameter(parameter))
            setLoggingTrue();
        else if (isPortParameter(parameter))
            setPort();
        else if (isFileNameNotYetSet())
            setFileName(parameter);
    }

    private void setPort() {
        validateParameterAfterPortFlag();
    }

    private void invalidPortNumberError() {
        throw new InvalidPortNumberException();
    }

    private void validateParameterAfterPortFlag(){
        String nextParameter = getParameterAfterPortFlag();
        if(!isNumber(nextParameter))
            portNumberMissingError();
        else
            invalidPortNumberError();
    }

    private String getParameterAfterPortFlag() {
        Iterator<String> it = parameters.iterator();
        while (it.hasNext()){
            String nextParameter = it.next();
            if(nextParameter.equals(PORT_FLAG) && it.hasNext()){
                return it.next();
            }
        }
        return null;
    }

    private void portNumberMissingError() {
        throw new PortValueMissingException();
    }

    private boolean isPortParameter(String parameter) {
        return PORT_FLAG.equals(parameter);
    }

    private void unknownParameterError(int position, String parameter) {
        throw new UnknownParameterException(position, parameter);
    }

    private boolean isFileNameNotYetSet() {
        return fileName.equals(DEFAULT_FILE_NAME);
    }

    private boolean isLoggingParameter(String parameter) {
        return parameter.equals(LOGGING_FLAG) && !isLogging;
    }

    private void setLoggingTrue(){
        this.isLogging = true;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getPort() {
        return port;
    }
}
