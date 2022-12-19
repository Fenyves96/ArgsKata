import exception.InvalidPortNumberException;
import exception.PortValueMissingException;
import exception.UnknownParameterException;
import hu.lechnerkozpont.kata.Args;
import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


public class ArgsNegativeTests {

    private Args args;

    @Before
    public void setUp() {
        args = new Args();
    }

    @Test(expected = IllegalParametersException.class)
    public void testSetParametersIfParametersIsNullThenIllegalParametersException(){
        args.setParameters(null);
    }


    @Test(expected = PortValueMissingException.class)
    public void testGetPortIfFlagGivenButParameter(){
        args.setParameters(new String []{"-p"});
    }

    @Test()
    public void testGetPortIfFlagGivenButParameterIsNotInteger(){
        assertThrows(PortValueMissingException.class, () -> args.setParameters(new String []{"-p", "filename.txt"}));
        assertThrows(PortValueMissingException.class, () -> args.setParameters(new String []{"-p", "88.7"}));
    }

    @Test()
    public void testGetPortIfPortNumberIsInvalidThenThrows(){
       assertThrows(InvalidPortNumberException.class, () -> args.setParameters(new String []{"-p", "-1"}));
       assertThrows(InvalidPortNumberException.class, () -> args.setParameters(new String []{"-p", "65537"}));
    }

    @Test
    public void testTooManyFileNameParameter(){
        UnknownParameterException actualException = assertThrows(UnknownParameterException.class, () ->
                args.setParameters(new String[]{"fileName.txt", "tooMuchFileName.txt"}));
        assertEquals("tooMuchFileName.txt", actualException.getParameter());
        assertEquals(2, actualException.getPosition());
    }

    @Test
    public void testTooManyParameter(){
        assertUknownParameter("-l", 3, new String[]{"-l", "fileName.txt", "-l"});
        assertUknownParameter("8080", 5, new String[]
                {"-l", "fileName.txt", "-p", "8080", "8080"});
        assertUknownParameter("-p", 4, new String[]{"-p", "8080", "-p", "-p"});
        assertUknownParameter("filename.txt", 4, new String[]
                {"-p", "8080", "filename.txt", "filename.txt"});
    }
    private void assertUknownParameter(String expectedParameter, int expectedPosition, String[] parameters) {
        UnknownParameterException ex2 = assertThrows(UnknownParameterException.class, () ->
                assertAllParameters("fileName.txt",8080,parameters));
        assertEquals(expectedParameter, ex2.getParameter());
        assertEquals(expectedPosition, ex2.getPosition());
    }

    private void assertAllParameters(String expectedFileName,int expectedPortNumber, String[] parameters) {
        assertLoggingParameterTrue(parameters);
        assertFileParameter(expectedFileName, parameters);
        assertPortNumber(expectedPortNumber, parameters);
    }

    private void assertLoggingParameterTrue(String[] parameters) {
        args.setParameters(parameters);
        assertTrue(args.isLogging());
    }

    private void assertFileParameter(String fileName, String[] parameters) {
        args.setParameters(parameters);
        assertEquals(fileName, args.getFileName());
    }

    private void assertPortNumber(int portNumber, String [] parameters) {
        args.setParameters(parameters);
        assertEquals(portNumber, args.getPort());
    }
}
