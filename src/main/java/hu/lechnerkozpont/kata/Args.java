package hu.lechnerkozpont.kata;

import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Args {
    private static final String PORT_FLAG = "-p";
    private static final String LOGGING_FLAG = "-l";

    private static final String DEFAULT_FILE_NAME = "";
    private static final boolean DEFAULT_NO_LOGGING = false;
    private static final int DEFAULT_PORT_NUMBER = 8080;


    private String fileName = null;
    private Boolean isLogging = null;
    private Integer port = null;
    List<String> parameters = new ArrayList<>();
    private String actualParameter;
    ListIterator<String> parametersIterator;


    public void setParameters(String[] parameters) {
        resetDefaults();
        check(parameters);
        collectUsefulParameters(parameters);
        parseAllParameters();
    }

    private void resetDefaults() {
        parameters = new ArrayList<>();
        setFileName(null);
        isLogging = null;
        port = null;
    }

    public String getFileName() {
        return fileName != null ? fileName : DEFAULT_FILE_NAME;
    }

    public boolean isLogging() {
        return isLogging != null ? isLogging : DEFAULT_NO_LOGGING;
    }

    public int getPort() {
        return port != null ? port : DEFAULT_PORT_NUMBER;
    }


    private void check(String[] parameters) {
        if(parameters == null)
            throw new IllegalParametersException();
        ArgsInputCheck argsInputCheck = new ArgsInputCheck();
        argsInputCheck.check(parameters);
    }
    private void collectUsefulParameters(String[] parameters) {
        for (String parameter: parameters) {
            if(StringUtils.isNotEmpty(parameter))
                this.parameters.add(parameter);
        }
    }

    private void parseAllParameters() {
        parametersIterator = parameters.listIterator();
        while (parametersIterator.hasNext()){
            parseActualParameter();
        }
    }
    private boolean isFileNameNotYetSet() {
        return Objects.equals(fileName, null);
    }
        private void parseActualParameter() {
        actualParameter = parametersIterator.next();
        if (PORT_FLAG.equals(actualParameter) && parametersIterator.hasNext()) {
            parametersIterator.next();
            setPort();
        }
        else if (isParameterUseableForLogging(actualParameter))
            setLoggingTrue();
        else if (isFileNameNotYetSet())
            setFileName(actualParameter);
    }

    private boolean isParameterUseableForLogging(String parameter) {
        return isLoggingParameter(parameter) && isLogging==null;
    }
    private boolean isLoggingParameter(String parameter) {
        return LOGGING_FLAG.equals(parameter);
    }

    private void setPort() {
        String nextParameter = getParameterAfterPortFlag();
        port = parseInt(nextParameter);
    }

    private int parseInt(String nextParameter) {
        return Integer.parseInt(Objects.requireNonNull(nextParameter));
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









    private void setLoggingTrue(){
        this.isLogging = true;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
