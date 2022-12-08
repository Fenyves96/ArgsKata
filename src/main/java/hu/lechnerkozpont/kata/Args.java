package hu.lechnerkozpont.kata;

import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Args {
    private static final String DEFAULT_FILE_NAME = "";
    private static final boolean DEFAULT_LOGGING = false;
    String fileName = DEFAULT_FILE_NAME;
    boolean logging = DEFAULT_LOGGING;
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
        logging = DEFAULT_LOGGING;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean getLogging() {
        return logging;
    }

    private void check(String[] parameters) {
        if(parameters == null)
            throw new IllegalParametersException();
    }

    private void collectUsefulParameters(String[] parameters) {
        for (String parameter: parameters) {
            if(StringUtils.isNotEmpty(parameter)){
                this.parameters.add(parameter);
            }
        }
    }

    private void parseAllParameters() {
        for (String parameter : parameters) {
            parseOneParameter(parameter);
        }
    }

    private void parseOneParameter(String parameter) {
        if (isLoggingParameter(parameter)) {
            setLoggingTrue();
        }
        else
            setFileName(parameter);
    }

    private boolean isLoggingParameter(String parameter) {
        return parameter.equals("-l") && !logging;
    }

    private void setLoggingTrue(){
        this.logging = true;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
