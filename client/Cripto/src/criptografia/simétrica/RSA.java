/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
      public static final String ALGORITHM = "RSA";
  
  /**
   * Local da chave privada no sistema de arquivos.
   */
  public static final String PATH_CHAVE_PRIVADA = "C:/keys/private.key";
  
  /**
   * Local da chave pública no sistema de arquivos.
   */
  public static final String PATH_CHAVE_PUBLICA = "C:/keys/public.key";
  
  /**
   * Gera a chave que contém um par de chave Privada e Pública usando 1025 bytes.
   * Armazena o conjunto de chaves nos arquivos private.key e public.key
   */
  public static void geraChave() {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(1024);
      final KeyPair key = keyGen.generateKeyPair();
  
      File chavePrivadaFile = new File(PATH_CHAVE_PRIVADA);
      File chavePublicaFile = new File(PATH_CHAVE_PUBLICA);
  
      // Cria os arquivos para armazenar a chave Privada e a chave Publica
      if (chavePrivadaFile.getParentFile() != null) {
        chavePrivadaFile.getParentFile().mkdirs();
      }
       
      chavePrivadaFile.createNewFile();
  
      if (chavePublicaFile.getParentFile() != null) {
        chavePublicaFile.getParentFile().mkdirs();
      }
       
      chavePublicaFile.createNewFile();
  
      // Salva a Chave Pública no arquivo
      ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
          new FileOutputStream(chavePublicaFile));
      chavePublicaOS.writeObject(key.getPublic());
      chavePublicaOS.close();
  
      // Salva a Chave Privada no arquivo
      ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(
          new FileOutputStream(chavePrivadaFile));
      chavePrivadaOS.writeObject(key.getPrivate());
      chavePrivadaOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  
  }
  
  
  
 private static byte[] getBytes(File file) {
        int len = (int) file.length();
        byte[] sendBuf = new byte[len];
        FileInputStream inFile = null;
        try {
            inFile = new FileInputStream(file);
            inFile.read(sendBuf, 0, len);

        } catch (FileNotFoundException fnfex) {

        } catch (IOException ioex) {

        }
        return sendBuf;
    }
  
  /**
   * Verifica se o par de chaves Pública e Privada já foram geradas.
   */
  public static boolean verificaSeExisteChavesNoSO() {
  
    File chavePrivada = new File(PATH_CHAVE_PRIVADA);
    File chavePublica = new File(PATH_CHAVE_PUBLICA);
  
    if (chavePrivada.exists() && chavePublica.exists()) {
      return true;
    }
     
    return false;
  }
  
  /**
   * Criptografa o texto puro usando chave pública.
   */
  public static void criptografa(File file, PublicKey chave,String dest) {
      
      
    try {
      
      byte[] resultFile = null;
      byte[] byteFile = getBytes(file);
     
   
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      // Criptografa o texto puro usando a chave Púlica
      cipher.init(Cipher.ENCRYPT_MODE, chave);
      
      byteFile = getBytes(file);
      
      resultFile = cipher.doFinal(byteFile);
      
        FileOutputStream arqOutC = new FileOutputStream(dest);
            DataOutputStream gravarArqC = new DataOutputStream(arqOutC);
            gravarArqC.write(resultFile);
            
            arqOutC.close();
            gravarArqC.close();

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            System.out.println("Erro cript");

        } catch (FileNotFoundException ex) {
           Logger.getLogger(CriptSim.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException | IllegalBlockSizeException | BadPaddingException ex) {
           Logger.getLogger(CriptSim.class.getName()).log(Level.SEVERE, null, ex);
       }
  //  return cipherText;
  }
  
  /**
   * Decriptografa o texto puro usando chave privada.
   */
  public static void decriptografa(File file, PrivateKey chave, String dest) {
   
     
    try {
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      // Decriptografa o texto puro usando a chave Privada
      cipher.init(Cipher.DECRYPT_MODE, chave);
      
      
     byte[] byteFile = getBytes(file);
            // Decriptografa o texto
        byte[] resultFile = cipher.doFinal(byteFile);
      //  System.out.println(""+chave);
  
      DataOutputStream gravarArqD;
        try (FileOutputStream arqOutD = new FileOutputStream(dest)) {
            gravarArqD = new DataOutputStream(arqOutD);
            gravarArqD.write(resultFile);
        }
            gravarArqD.close();
            

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Erro drecipt "+e);

        } catch (FileNotFoundException ex) {
          
       } catch (IOException ex) {
           
       }
  }
}

    