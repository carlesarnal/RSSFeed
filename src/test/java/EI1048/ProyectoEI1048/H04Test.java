package EI1048.ProyectoEI1048;

import junit.framework.TestCase;
import org.jdom.JDOMException;
import org.junit.Test;
import proyectoEI1048Modelo.BDD;

import java.io.IOException;

public class H04Test extends TestCase {
	@Test
	public void testBorrarCarpetaQueNoExiste () throws IOException, JDOMException {
        BDD bdd = new BDD();
        assertFalse(bdd.removeCarpeta("Carpeta Inexistente"));
	}
    @Test
    public void testBorrarCarpetaQueExisteYTieneCosasDentro () throws IOException, JDOMException {
        BDD bdd = new BDD();
        assertFalse(bdd.removeCarpeta("Carpeta Principal"));

    }
	@Test
	public void testBorrarCarpetaQueExisteYNoTieneCosasDentro () throws IOException, JDOMException {
        BDD bdd = new BDD();
        bdd.addCarpeta("Carpeta Nueva");
        assertTrue(bdd.removeCarpeta("Carpeta Nueva"));
	}
}