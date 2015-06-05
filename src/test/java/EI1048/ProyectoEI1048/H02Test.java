package EI1048.ProyectoEI1048;

import java.io.IOException;
import junit.framework.TestCase;
import org.jdom.JDOMException;
import org.junit.Test;
import proyectoEI1048Modelo.BDD;

public class H02Test extends TestCase {
	@Test
	public void testBorrarFuenteQueNoExiste () throws IOException, JDOMException {
        BDD bdd = new BDD();
        assertFalse(bdd.removeFuente("El País Delincuencia", "mñkkññ","Carpeta Principal"));
	}
	@Test
	public void testBorrarFuenteQueExisteCorrectamente () throws IOException, JDOMException {
        BDD bdd = new BDD();
        bdd.addFuente("El País Delincuencia", "mñkkññ","Carpeta Principal");
        assertTrue( bdd.removeFuente("El País Delincuencia", "mñkkññ","Carpeta Principal"));

	}
}
