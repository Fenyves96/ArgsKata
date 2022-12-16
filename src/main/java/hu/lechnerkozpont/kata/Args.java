package hu.lechnerkozpont.kata;

import exception.InvalidPortNumberException;
import exception.PortValueMissingException;
import exception.UnknownParameterException;
import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Args {
    private static final String PORT_FLAG = "-p";
    private static final String LOGGING_FLAG = "-l";

    private static final String DEFAULT_FILE_NAME = "";
    private static final boolean DEFAULT_NO_LOGGING = false;
    private static final int DEFAULT_PORT_NUMBER = 8080;
    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65536;

    private String fileName = null;
    private Boolean isLogging = null;
    private Integer port = null;
    List<String> parameters = new ArrayList<>();


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
    }

    private void collectUsefulParameters(String[] parameters) {
        for (String parameter: parameters) {
            if(StringUtils.isNotEmpty(parameter))
                this.parameters.add(parameter);
        }
    }

    private void parseAllParameters() {
        ListIterator<String> it = parameters.listIterator();
        while (it.hasNext()){
            String parameter = it.next();
            ifUknownParameterThenThrows(it.nextIndex(), parameter);
            parseOneParameter(parameter);
            if(isPortParameter(parameter))
                it.next();
        }
    }

    private void ifUknownParameterThenThrows(int position, String parameter) {
        if(!isParameterUseAble(parameter))
            unknownParameterError(position, parameter);
    }

    private boolean isParameterUseAble(String parameter) {
        return isParameterUseableForLogging(parameter) || isParameterUseableForPort(parameter) || isFileNameNotYetSet();
    }

    private boolean isParameterUseableForPort(String parameter) {
        return isPortParameter(parameter) && port == null;
    }

    private boolean isParameterUseableForLogging(String parameter) {
        return isLoggingParameter(parameter) && isLogging == null;
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
        String nextParameter = getParameterAfterPortFlag();
        validateParameterAfterPortFlag(nextParameter);
        port = parseInt(nextParameter);
    }

    private int parseInt(String nextParameter) {
        return Integer.parseInt(Objects.requireNonNull(nextParameter));
    }

    private void invalidPortNumberError() {
        throw new InvalidPortNumberException();
    }

    private void validateParameterAfterPortFlag(String parameter){
        if(!isInteger(parameter))
            portNumberMissingError();
        validatePortNumber(parameter);
    }

    private void validatePortNumber(String nextParameter) {
        int inputPortNumber = Integer.parseInt(nextParameter);
        if(!isPortNumberBetweenRange(inputPortNumber))
            invalidPortNumberError();
    }

    private boolean isInteger(String inputString) {
            try {
                Integer.parseInt(inputString);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
    }

    private boolean isPortNumberBetweenRange(int inputPortNumber) {
        return inputPortNumber >= MIN_PORT_NUMBER && inputPortNumber <= MAX_PORT_NUMBER;
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
        return Objects.equals(fileName, null);
    }

    private boolean isLoggingParameter(String parameter) {
        return parameter.equals(LOGGING_FLAG) && !Optional.ofNullable(isLogging).orElse(false);
    }

    private void setLoggingTrue(){
        this.isLogging = true;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
