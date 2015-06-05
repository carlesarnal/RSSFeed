package EI1048.ProyectoEI1048;

import java.io.IOException;
import junit.framework.TestCase;
import org.jdom.JDOMException;
import org.junit.Test;
import org.xml.sax.SAXException;
import proyectoEI1048Modelo.BDD;
import javax.xml.parsers.ParserConfigurationException;

public class H01Test extends TestCase {
    @Test
	public void testInsertarFuenteQueYaExiste () throws IOException, SAXException, ParserConfigurationException, JDOMException {
        BDD bdd = new BDD();
        assertFalse(bdd.addFuente("El País Economia", "http://ep00.epimg.net/rss/economia/portada.xml","Carpeta Principal"));
	}
    @Test
	public void testInsertarUnaFuenteQueNoExiste () throws IOException, SAXException, ParserConfigurationException, JDOMException {
        BDD bdd = new BDD();
        assertTrue(bdd.addFuente("El País Gitaneria", "http://ep00.epimg.net/rss/gitaneria/portada.xml","Carpeta Principal"));
        bdd.removeFuente("El País Gitaneria", "http://ep00.epimg.net/rss/gitaneria/portada.xml","Carpeta Principal");
	}
    @Test
    public void testInsertarFuenteEnUnaCarpetaQueNoExiste () throws IOException, SAXException, ParserConfigurationException, JDOMException {
        BDD bdd = new BDD();
        assertFalse(bdd.addFuente("El País Economia", "http://ep00.epimg.net/rss/economia/portada.xml","Carpeta Inexistente"));
    }
}