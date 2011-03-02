package repo.txnmap;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import repo.txnmap.generated.Root;

import com.google.gson.Gson;
 

/**
 * To test if nrow_txnmap.xml is read properly
 * @author Samarjit
 *
 */
public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final JAXBContext jc = JAXBContext.newInstance(Root.class);
			final Root root =
	            (Root) jc.createUnmarshaller().unmarshal(
	                new File("C:/Eclipse/workspace1/FEtranslator1/src/repo/txnmap/nrow_txnmap.xml"));
			
			jc.createMarshaller().marshal(root,System.out);
			Gson gson = new Gson();
			System.out.println();
			System.out.println(gson.toJson(root));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
