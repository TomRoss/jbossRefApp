/*
 * Copyright (c) 2012 by Lombard Odier Darier Hentsch, Geneva, Switzerland.
 * This software is subject to copyright protection under the laws of
 * Switzerland and other countries.  ALL RIGHTS RESERVED.
 * 
 */

package org.app.minibank.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.jboss.security.Base64Utils;
import org.jboss.security.vault.SecurityVaultException;

/**
 * @version $Revision:$.
 * @author arteconsult.
 * @last.author $Author:$.
 * @last.checkin $Date:$.
 */
public class CustomSecurityVault implements org.jboss.security.vault.SecurityVault {

    public static final String KEYSTORE_URL = "KEYSTORE_URL";

    public static final String KEY_FACTORY = "KEY_FACTORY";

    public static final String KEY_ALGORITHM = "KEY_ALGORITHM";

    public static final String ITERATION_COUNT = "ITERATION_COUNT";

    public static final String SALT = "SALT";

    public static final String KEYSTORE_PASSPHRASE = "KEYSTORE_PASSPHRASE";

    public static final String CIPHER_ALGORITHM = "CIPHER_ALGORITHM";

    private static final Logger log = Logger.getLogger(CustomSecurityVault.class);

    private String cipherAlgorithm = "AES/ECB/PKCS5Padding";

    private SecretKeySpec key;

    private boolean isInitialized = false;

    private Properties securityProperties;

    private String keystoreUrl;

    @Override
    public void init(Map<String, Object> options) throws SecurityVaultException {

        if (options.containsKey(CIPHER_ALGORITHM)) cipherAlgorithm = (String) options.get(CIPHER_ALGORITHM);
        if (options.containsKey(KEYSTORE_PASSPHRASE) && options.containsKey(SALT) && options.containsKey(ITERATION_COUNT)) {
            if (options.containsKey(KEY_ALGORITHM)) {

                try {
                    initKey((String) options.get(KEYSTORE_PASSPHRASE), (String) options.get(SALT),
                            new Integer((String) options.get(ITERATION_COUNT)).intValue(), (String) options.get(KEY_FACTORY),
                            (String) options.get(KEY_ALGORITHM));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new SecurityVaultException(e);
                }
            } else {
                try {
                    initKey((String) options.get(KEYSTORE_PASSPHRASE), (String) options.get(SALT),
                            new Integer((String) options.get(ITERATION_COUNT)).intValue(), (String) options.get(KEY_FACTORY), "AES");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new SecurityVaultException(e);
                }
            }
        } else {
            throw new SecurityVaultException("Missing cipher parameters to initialize vault");
        }
        if (options.containsKey(KEYSTORE_URL)) {
            keystoreUrl = (String) options.get(KEYSTORE_URL);
            securityProperties = new Properties();
            File file = new File(keystoreUrl);

            if (file.canRead()) {

                FileInputStream in;
                try {
                    in = new FileInputStream(keystoreUrl);
                    securityProperties.load(in);
                    in.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new SecurityVaultException(e);
                }
            }

        } else {
            throw new SecurityVaultException("Missing keystore URL to initialize vault");
        }

        isInitialized = true;
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public byte[] handshake(Map<String, Object> handshakeOptions) throws SecurityVaultException {
        // TODO Auto-generated method stub
        log.info(handshakeOptions);
        return null;
    }

    @Override
    public Set<String> keyList() throws SecurityVaultException {
        return securityProperties.stringPropertyNames();
    }

    @Override
    public boolean exists(String vaultBlock, String attributeName) throws SecurityVaultException {
        return securityProperties.containsKey(vaultBlock + "." + attributeName);
    }

    @Override
    public void store(String vaultBlock, String attributeName, char[] attributeValue, byte[] sharedKey) throws SecurityVaultException {
        if (!isInitialized) throw new SecurityVaultException("Vault is not initialized");
        try {
            securityProperties.setProperty(vaultBlock + "." + attributeName, new String(encrpyt(new String(attributeValue))));
            updateKeystoreFile("added: " + vaultBlock + "." + attributeName);
            log.info("added: " + vaultBlock + "." + attributeName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SecurityVaultException(e);
        }

    }

    @Override
    public char[] retrieve(String vaultBlock, String attributeName, byte[] sharedKey) throws SecurityVaultException {
        if (!isInitialized) throw new SecurityVaultException("Vault is not initialized");
        if (!exists(vaultBlock, attributeName)) throw new SecurityVaultException("Vault does not contain " + vaultBlock + "." + attributeName);
        try {
            return new String(decrypt(securityProperties.getProperty(vaultBlock + "." + attributeName))).toCharArray();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SecurityVaultException(e);
        }
    }

    @Override
    public boolean remove(String vaultBlock, String attributeName, byte[] sharedKey) throws SecurityVaultException {
        if (!isInitialized) throw new SecurityVaultException("Vault is not initialized");
        try {
            if (securityProperties.containsKey(vaultBlock + "." + attributeName)) {
                securityProperties.remove(vaultBlock + "." + attributeName);
                updateKeystoreFile("removed: " + vaultBlock + "." + attributeName);
                log.info("removed: " + vaultBlock + "." + attributeName);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SecurityVaultException(e);
        }
    }

    private String encrpyt(String cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64Utils.tob64(cipher.doFinal(cleartext.getBytes("UTF-8")));

    }

    private byte[] decrypt(String encrptedText) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(Base64Utils.fromb64(encrptedText));
    }

    private void initKey(String passphrase, String salt, int iterations, String keyFactory, String keyAlgorithm) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(keyFactory);
        SecretKey tmp = factory.generateSecret(new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterations, 128));
        key = new SecretKeySpec(tmp.getEncoded(), keyAlgorithm);
    }

    private void updateKeystoreFile(String comments) throws Exception {
        FileOutputStream out = new FileOutputStream(keystoreUrl);
        securityProperties.store(out, comments);
        out.close();
    }

}
