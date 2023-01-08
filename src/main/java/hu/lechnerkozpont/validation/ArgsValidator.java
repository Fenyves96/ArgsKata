package hu.lechnerkozpont.validation;

import exception.InvalidPortNumberException;
import exception.PortValueMissingException;
import exception.UnknownParameterException;
import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ArgsValidator {
    boolean portParameterIsAlreadyFound = false;
    boolean loggingAlreadyFound = false;
    boolean fileNameAlreadyFound = false;
    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65536;

    public void check(String[] parameters) {
        if (parameters == null)
            throw new IllegalParametersException();
        List<String> parameterList = getNotNUllParameters(parameters);
        ListIterator<String> it = parameterList.listIterator();
        while (it.hasNext()) {
            String parameter = it.next();
            if ("-p".equals(parameter) && !portParameterIsAlreadyFound) {
                checkPort(it);
                portParameterIsAlreadyFound = true;
            } else if ("-l".equals(parameter) && !loggingAlreadyFound)
                loggingAlreadyFound = true;
            else if (!fileNameAlreadyFound)
                fileNameAlreadyFound = true;
            else {
                unknownParameterError(it.nextIndex(), parameter);
            }
        }
    }

    private void checkPort(ListIterator<String> it) {
        if (!it.hasNext())
            portNumberMissingError();
        String portValue = it.next();
        checkPortValue(portValue);
    }

    private void checkPortValue(String portValue) {
        if (!isInteger(portValue))
            throw new PortValueMissingException();
        if (!isPortNumberBetweenRange(Integer.parseInt(portValue)))
            invalidPortNumberError();
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

    private void portNumberMissingError() {
        throw new PortValueMissingException();
    }

    private List<String> getNotNUllParameters(String[] parameters) {
        List<String> notNUllParameters = new ArrayList<>();
        for (String parameter : parameters) {
            if (StringUtils.isNotEmpty(parameter))
                notNUllParameters.add(parameter);
        }
        return notNUllParameters;
    }

    private void invalidPortNumberError() {
        throw new InvalidPortNumberException();
    }

    private void unknownParameterError(int index, String parameter) {
        throw new UnknownParameterException(index, parameter);
    }

}
