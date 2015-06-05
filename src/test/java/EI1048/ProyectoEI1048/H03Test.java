package EI1048.ProyectoEI1048;

import junit.framework.TestCase;
import org.jdom.JDOMException;
import org.junit.Test;
import proyectoEI1048Modelo.BDD;

import java.io.IOException;

public class H03Test extends TestCase {
	@Test
	public void testInsertarCarpetaConNombreRepetido () throws IOException, JDOMException {
        BDD bdd = new BDD();
        assertFalse(bdd.addCarpeta("Carpeta Principal"));
	}
	@Test
	public void testInsertarCarpetaConNombreNoRepetido () throws IOException, JDOMException {
        BDD bdd = new BDD();
        assertTrue(bdd.addCarpeta("Carpeta Nueva"));
        bdd.removeCarpeta("Carpeta Nueva");
	}
}
