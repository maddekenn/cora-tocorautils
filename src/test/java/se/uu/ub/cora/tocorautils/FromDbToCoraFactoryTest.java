package se.uu.ub.cora.tocorautils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.client.CoraClientConfig;
import se.uu.ub.cora.connection.ParameterConnectionProviderImp;
import se.uu.ub.cora.sqldatabase.RecordReaderFactoryImp;
import se.uu.ub.cora.tocorautils.doubles.CoraClientFactorySpy;
import se.uu.ub.cora.tocorautils.doubles.FromDbToCoraFactorySpy;
import se.uu.ub.cora.tocorautils.importing.CoraImporter;

public class FromDbToCoraFactoryTest {

	private se.uu.ub.cora.tocorautils.DbConfig dbConfig;
	private CoraClientConfig coraClientConfig;
	private FromDbToCoraFactorySpy factorySpyUsedToTestParentAbstractClass;
	private CoraClientFactorySpy coraClientFactory;

	@BeforeMethod
	public void setup() {
		coraClientFactory = new CoraClientFactorySpy();
		setUpClientConfig();
		setUpDbConfig();
		factorySpyUsedToTestParentAbstractClass = new FromDbToCoraFactorySpy();

	}

	@Test
	public void factor() {
		ListFromDbToCoraImp fromDbToCora = (ListFromDbToCoraImp) factorySpyUsedToTestParentAbstractClass
				.factorFromDbToCora(coraClientFactory, coraClientConfig, dbConfig);

		assertEquals(factorySpyUsedToTestParentAbstractClass.getCoraClientFactory(),
				coraClientFactory);

		assertEquals(fromDbToCora.getFromDbToCoraConverter(),
				factorySpyUsedToTestParentAbstractClass.createdConverter);

		RecordReaderFactoryImp recordReaderFactory = (RecordReaderFactoryImp) fromDbToCora
				.getRecordReaderFactory();
		assertTrue(recordReaderFactory
				.getConnectionProvider() instanceof ParameterConnectionProviderImp);

		assertTrue(fromDbToCora.getImporter() instanceof CoraImporter);
		CoraImporter importer = (CoraImporter) fromDbToCora.getImporter();
		assertEquals(importer.getCoraClient(), coraClientFactory.factored);

	}

	private void setUpDbConfig() {
		String dbUserId = "someDbUserId";
		String password = "someDbPassword";
		String url = "someDbUrl";
		dbConfig = new DbConfig(dbUserId, password, url);
	}

	private void setUpClientConfig() {
		String userId = "someCoraUserId";
		String appToken = "someCoraAppToken";
		String appTokenVerifierUrl = "someCoraAppTokenVierifierUrl";
		String coraUrl = "someCoraUrl";
		coraClientConfig = new CoraClientConfig(userId, appToken, appTokenVerifierUrl, coraUrl);
	}
}
