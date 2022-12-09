package hu.lechnerkozpont.kata;

import exception.UnknownParameterException;
import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Args {
    private static final String DEFAULT_FILE_NAME = "";
    private static final boolean DEFAULT_NO_LOGGING = false;
    String fileName = DEFAULT_FILE_NAME;
    boolean isLogging = DEFAULT_NO_LOGGING;
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
        return !isFileNameDefault();
    }

    private void parseOneParameter(String parameter) {
        if (isLoggingParameter(parameter))
            setLoggingTrue();
        else if (isFileNameDefault())
            setFileName(parameter);
    }

    private void unknownParameterError(int position, String parameter) {
        throw new UnknownParameterException(position, parameter);
    }

    private boolean isFileNameDefault() {
        return fileName.equals(DEFAULT_FILE_NAME);
    }

    private boolean isLoggingParameter(String parameter) {
        return parameter.equals("-l") && !isLogging;
    }

    private void setLoggingTrue(){
        this.isLogging = true;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
