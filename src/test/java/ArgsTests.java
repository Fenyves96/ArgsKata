import exception.InvalidPortNumberException;
import exception.PortValueMissingException;
import exception.UnknownParameterException;
import hu.lechnerkozpont.kata.Args;
import hu.lechnerkozpont.kata.exception.IllegalParametersException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ArgsTests {

    private Args args;

    @Before
    public void setUp() {
        args = new Args();
    }

    @Test(expected = IllegalParametersException.class)
    public void testSetParametersIfParametersIsNullThenIllegalParametersException(){
        args.setParameters(null);
    }

    @Test
    public void testGetFileNameIfNullFileNameThenEmptyString(){
        assertFileParameter("", new String[] {null});
    }

    @Test
    public void testGetFileNameIfEmptyStringThenEmptyString(){
        assertFileParameter("", new String[] {""});
    }

    @Test
    public void testGetFileNameIfNoInputThenEmptyString(){
        assertFileParameter("", new String[] {});
    }

    @Test
    public void testGetFileNameIfFileNameGivenThenGivenFileName(){
        assertFileParameter("validFileName.txt",
                new String[] {"validFileName.txt"});
    }

    @Test
    public void testGetFileNameIfNotFirstThenGivenFileName(){
        assertFileParameter("validFileName.txt",
                new String[] {"", "validFileName.txt"});
    }

    @Test
    public void testGetFileNameIfFileNameHasNoExtensionThenFileName(){
        assertFileParameter("validFileNameWithoutExtension",
                new String[] {"validFileNameWithoutExtension"});
    }

    @Test
    public void testGetFileNameIfFileNameLikeFlagThenFileName(){
        assertFileParameter("-d", new String[] {"-d"});
    }

    @Test
    public void testSetFileNameWhenDoubleCallThenSecondFileName(){
        args.setParameters(new String[] {"filename1.txt"});
        assertEquals("filename1.txt", args.getFileName());
        args.setParameters(new String[] {"filename2.txt"});
        assertEquals("filename2.txt", args.getFileName());
    }

    @Test
    public void testGetLoggingIfFlagNotGivenThenFalse(){
        assertLoggingParameterFalse(new String[]{""});
        assertLoggingParameterFalse(new String [] {});
        assertLoggingParameterFalse(new String [] {"filname.txt"});
        assertLoggingParameterFalse(new String[] {"-p", "9090", "filename.txt"});
    }

    @Test
    public void testGetLoggingIfFlagGivenThenTrue(){
        assertLoggingParameterTrue(new String[]{"-l"});
        assertLoggingParameterTrue(new String[]{"-l", ""});
        assertLoggingParameterTrue(new String[]{"", "-l"});
    }

    @Test
    public void testGetLoggingIfFlagGiven(){
        assertAllParameters("filename.txt", 8080,
                new String[]{"filename.txt","-l"});
        assertAllParameters("filename.txt", 8080,
                new String[]{"-l", "filename.txt"});
        assertAllParameters("-l", 8080, new String[]{"-l", "-l"});
    }

    @Test
    public void testGetPortIfPortNotGiven(){
        args.setParameters(new String []{""});
        assertEquals(8080, args.getPort());
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
    public void testGetPortNumberWithValidNumbers(){
        assertPortNumber(0, new String []{"-p", "0"});
        assertPortNumber(9090, new String []{"-p", "9090"});
        assertPortNumber(65536, new String []{"-p", "65536"});


        assertPortNumber(1234, new String []{"-p", "1234", "filename.txt"});
        assertPortNumber(1234, new String []{"-p", "1234", "filename.txt", "-l"});
        assertAllParameters("filename.txt", 9876, new String []
                {"-l", "-p", "9876", "filename.txt"});
    }

    private void assertPortNumber(int portNumber, String [] parameters) {
        args.setParameters(parameters);
        assertEquals(portNumber, args.getPort());
    }

    @Test
    public void testTooManyParameter(){
        UnknownParameterException ex = assertThrows(UnknownParameterException.class, () ->
                assertAllParameters("fileName.txt",8080,new String[]{"-l","fileName.txt","-l"}));
        assertEquals("-l", ex.getParameter());
        assertEquals(3, ex.getPosition());
    }

    private void assertAllParameters(String expectedFileName,int expectedPortNumber, String[] parameters) {
        assertLoggingParameterTrue(parameters);
        assertFileParameter(expectedFileName, parameters);
        assertPortNumber(expectedPortNumber, parameters);
    }

    private void assertLoggingParameterFalse(String[] parameters) {
        args.setParameters(parameters);
        assertFalse(args.isLogging());
    }

    private void assertLoggingParameterTrue(String[] parameters) {
        args.setParameters(parameters);
        assertTrue(args.isLogging());
    }

    private void assertFileParameter(String fileName, String[] parameters) {
        args.setParameters(parameters);
        assertEquals(fileName, args.getFileName());
    }


}
