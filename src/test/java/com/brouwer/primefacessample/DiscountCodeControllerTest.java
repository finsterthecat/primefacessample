/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brouwer.primefacessample;

import com.brouwer.primefacessample.model.DiscountCode;
import com.brouwer.util.FacesContextMocker;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * Mocked out tests for DiscountCodeController.
 * 
 * @author tonybrouwer
 */
public class DiscountCodeControllerTest {
  DiscountCodeController cut;
  DiscountCode TEST_DISCOUNT_CODE = new DiscountCode("A", new BigDecimal("2.2"));
  
  @Before
  public void initializeDependencies() {
    this.cut = new DiscountCodeController();
    this.cut.ejbFacade = mock(DiscountCodeFacade.class);
    this.cut.prepareCreate();
    this.cut.setSelected(TEST_DISCOUNT_CODE);
  }
  
  @Test
  public void shouldPrepareCreate() throws Exception {
    DiscountCode dc = this.cut.prepareCreate();
    assertNull(dc.getDiscountCode());
    assertNull(dc.getRate());
  }
  
  @Test
  public void shouldGetSelected() throws Exception {
    DiscountCode dc = this.cut.getSelected();
    assertEquals("A", dc.getDiscountCode());
    assertEquals(new BigDecimal("2.2"), dc.getRate());
  }
  
  @Test
  public void shouldGetDiscountCode() throws Exception {
    DiscountCode dc = this.cut.getDiscountCode("A");
    assertNull(dc);
  }
  
  @Test
  public void shouldCreate() throws Exception {
    FacesContext context = null;
    
    try {
      context = initializeFacesContext();
      this.cut.create();
      verify(this.cut.ejbFacade).edit(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }

  @Test
  public void shouldRemove() throws Exception {
    FacesContext context = null;
    try {
      context = initializeFacesContext();
      this.cut.destroy();
      verify(this.cut.ejbFacade).remove(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }

  //Lather, rinse, repeat for remaining tests...
  
  /**
   * FacesContext is used to return messages to the user, get request parameters, etc.
   * 
   * @param context 
   */
  private FacesContext initializeFacesContext() {
    FacesContext context = FacesContextMocker.mockFacesContext();
    Map<String, String> requestParameters = new HashMap<>();
    ExternalContext ext = mock(ExternalContext.class);
    when(context.getExternalContext()).thenReturn(ext);
    when(ext.getRequestParameterMap()).thenReturn(requestParameters);
    return context;
  }
}
