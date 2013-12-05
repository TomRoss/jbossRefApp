package org.app.minibank.security;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class TestCustomSecurityVault {

    private CustomSecurityVault csv = null;

    private String testKeystoreUrl = "target/test-classes/customVault.properties";

    @Before
    public void init() throws Exception {

        csv = new CustomSecurityVault();

        Map<String, Object> options = new HashMap<String, Object>();
        options.put(CustomSecurityVault.CIPHER_ALGORITHM, "AES/ECB/PKCS5Padding");
        options.put(CustomSecurityVault.ITERATION_COUNT, "54321");
        options.put(CustomSecurityVault.KEY_ALGORITHM, "AES");
        options.put(CustomSecurityVault.KEY_FACTORY, "PBKDF2WithHmacSHA1");
        options.put(CustomSecurityVault.KEYSTORE_PASSPHRASE, "we grow when we share");
        options.put(CustomSecurityVault.KEYSTORE_URL, testKeystoreUrl);
        options.put(CustomSecurityVault.SALT, "I like enterprise ready products");

        csv.init(options);
    }

    @Test
    public void testStore() throws Exception {
        String vaultBlock = "vaultBlock";
        String attributeName = "secret1";
        String attributeValueString = "my secret to store";

        csv.store(vaultBlock, attributeName, attributeValueString.toCharArray(), null);

        Properties securityVaultProps = new Properties();
        FileInputStream in;
        in = new FileInputStream(testKeystoreUrl);
        securityVaultProps.load(in);
        in.close();

        assertTrue(securityVaultProps.containsKey(vaultBlock + "." + attributeName));
    }

    @Test
    public void testRetrieve() throws Exception {
        String vaultBlock = "vaultBlock";
        String attributeName = "secret2";
        String attributeValueString = "another secret to store";

        csv.store(vaultBlock, attributeName, attributeValueString.toCharArray(), null);

        String retrievedString = new String(csv.retrieve(vaultBlock, attributeName, null));

        assertEquals(attributeValueString, retrievedString);
    }

    @Test
    public void testRemove() throws Exception {
        String vaultBlock = "vaultBlock";
        String attributeName = "secret3";
        String attributeValueString = "another secret to store";

        csv.store(vaultBlock, attributeName, attributeValueString.toCharArray(), null);

        csv.remove(vaultBlock, attributeName, null);

        Properties securityVaultProps = new Properties();
        FileInputStream in;
        in = new FileInputStream(testKeystoreUrl);
        securityVaultProps.load(in);
        in.close();

        assertFalse(securityVaultProps.containsKey(vaultBlock + "." + attributeName));
    }

    @Test
    public void createDummyEntry4DB() throws Exception {
        String vaultBlock = "ds_ExampleWithVaultDS";
        String attributeName = "password";
        String attributeValueString = "my secret database password";

        csv.store(vaultBlock, attributeName, attributeValueString.toCharArray(), null);

        Properties securityVaultProps = new Properties();
        FileInputStream in;
        in = new FileInputStream(testKeystoreUrl);
        securityVaultProps.load(in);
        in.close();

        assertTrue(securityVaultProps.containsKey(vaultBlock + "." + attributeName));

        String retrievedString = new String(csv.retrieve(vaultBlock, attributeName, null));

        assertEquals(attributeValueString, retrievedString);
    }
}
