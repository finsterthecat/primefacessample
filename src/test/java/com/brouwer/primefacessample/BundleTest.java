/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brouwer.primefacessample;

import java.util.ResourceBundle;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test that Resource bundle Bundle works.
 * @author tonybrouwer
 */
public class BundleTest {
  
  @Test
  public void shouldLookupExistingEntry() {
    String b = ResourceBundle.getBundle("Bundle")
                .getString("PersistenceErrorOccured");
    assertEquals("A persistence error occurred.", b);
  }
  
}
