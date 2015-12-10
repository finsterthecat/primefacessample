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
import java.util.List;
import java.util.Map;
import javax.ejb.EJBException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
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
  /**
   * Initialize dependencies for all tests. Need a controller to test. The controller needs an ejbFacade mock to call.
   * We also have the facade select a discount code.
   * @throws Exception the exception
   */
  public void initializeDependencies() {
    this.cut = new DiscountCodeController();
    this.cut.ejbFacade = mock(DiscountCodeFacade.class);
    this.cut.prepareCreate();
    this.cut.setSelected(TEST_DISCOUNT_CODE);
  }
  
  @Test
  /**
   * PrepareCreate mock will initialize to a DiscountCode that's all null.
   * @throws Exception the exception
   */
  public void shouldPrepareCreate() throws Exception {
    DiscountCode dc = this.cut.prepareCreate();
    assertNull(dc.getDiscountCode());
    assertNull(dc.getRate());
  }
  
  @Test
  /**
   * Make sure that setting selected will set the discount code to what's passed in.
   * @throws Exception the exception
   */
  public void shouldGetSelected() throws Exception {
    DiscountCode dc = this.cut.getSelected();
    assertEquals("A", dc.getDiscountCode());
    assertEquals(new BigDecimal("2.2"), dc.getRate());
  }
  
  @Test
  /**
   * GetDiscountCode should invoke facade dot find.
   * @throws Exception the exception
   */
  public void shouldGetDiscountCode() throws Exception {
    DiscountCode dc = this.cut.getDiscountCode("A");
    verify(this.cut.ejbFacade).find("A");
    assertNull(dc);
  }
  
  @Test
  /**
   * Get available methods both call findAll.
   * @throws Exception  the exception
   */
  public void shouldItemsGetAvailable() throws Exception {
    this.cut.getItemsAvailableSelectMany();
    this.cut.getItemsAvailableSelectOne();
    verify(this.cut.ejbFacade, times(2)).findAll();
  }
  
  @Test
  /**
   * Create method should call edit on the facade.
   * @throws Exception the exception
   */
  public void shouldCreate() throws Exception {
    FacesContext context = null;
    
    try {
      context = initializeFacesContext();
      when(context.isValidationFailed()).thenReturn(Boolean.FALSE);
      this.cut.create();
      verify(this.cut.ejbFacade).edit(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }
  
  @Test
  /**
   * Create method should call edit on the facade.
   * @throws Exception the exception
   */
  public void shouldCreateInvalid() throws Exception {
    FacesContext context = null;
    
    try {
      context = initializeFacesContext();
      when(context.isValidationFailed()).thenReturn(Boolean.TRUE);
      this.cut.create();
      verify(this.cut.ejbFacade).edit(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }
  
  @Test
  /**
   * Create method will store some messages in FacesContext if the facade edit method returns an EJBException.
   * TODO query those messages from JSFUtil
   * @throws Exception the exception
   */
  public void shouldCreateEJBException() throws Exception {
    FacesContext context = null;
    
    try {
      context = initializeFacesContext();
      doThrow(new EJBException(new NoResultException("No results, guv."))).when(this.cut.ejbFacade).edit(TEST_DISCOUNT_CODE);
      this.cut.create();
      verify(this.cut.ejbFacade, times(1)).edit(TEST_DISCOUNT_CODE);
      //Code coverage... exercizes the empty error string path
      doThrow(new EJBException(new NoResultException(""))).when(this.cut.ejbFacade).edit(TEST_DISCOUNT_CODE);
      this.cut.create();
      verify(this.cut.ejbFacade, times(2)).edit(TEST_DISCOUNT_CODE);
      //Code coverage... exercizes the empty cause path
      doThrow(new EJBException()).when(this.cut.ejbFacade).edit(TEST_DISCOUNT_CODE);
      this.cut.create();
      verify(this.cut.ejbFacade, times(3)).edit(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }
  
  @Test
  /**
   * Create method should not call edit on the facade if selected is null.
   * @throws Exception the exception
   */
  public void shouldCreateNullNotEdit() throws Exception {
    FacesContext context = null;
    
    try {
      context = initializeFacesContext();
      this.cut.setSelected(null);
      this.cut.create();
      verify(this.cut.ejbFacade, times(0)).edit(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }

  @Test
  /**
   * Update method should call edit on the facade.
   * @throws Exception the exception
   */
  public void shouldUpdate() throws Exception {
    FacesContext context = null;
    
    try {
      context = initializeFacesContext();
      this.cut.update();
      verify(this.cut.ejbFacade).edit(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }

  @Test
  /**
   * Update method should call edit on the facade.
   * @throws Exception the exception
   */
  public void shouldFindAll() throws Exception {
    FacesContext context = null;
    
    try {
      context = initializeFacesContext();
      List<DiscountCode> dcs = this.cut.getItems();
      verify(this.cut.ejbFacade, times(1)).findAll();
      dcs = this.cut.getItems(); //Should not trigger a second call to findAll
      verify(this.cut.ejbFacade, times(1)).findAll();
    } finally {
      if (context != null) context.release();
    }
    
  }

  @Test
  /**
   * Destroy method should call remove on the facade.
   * @throws Exception the exception
   */
  public void shouldDestroy() throws Exception {
    FacesContext context = null;
    try {
      context = initializeFacesContext();
      when(context.isValidationFailed()).thenReturn(Boolean.FALSE);
      this.cut.destroy();
      verify(this.cut.ejbFacade).remove(TEST_DISCOUNT_CODE);
      assertNull(this.cut.getSelected());
    } finally {
      if (context != null) context.release();
    }
    
  }

  @Test
  /**
   * Destroy method for null selected should not call remove on the facade.
   * @throws Exception the exception
   */
  public void shouldDestroyNullNotRemove() throws Exception {
    FacesContext context = null;
    try {
      context = initializeFacesContext();
      this.cut.setSelected(null);
      this.cut.destroy();
      verify(this.cut.ejbFacade, times(0)).remove(TEST_DISCOUNT_CODE);
    } finally {
      if (context != null) context.release();
    }
    
  }

  @Test
  /**
   * Destroy method for null selected should not call remove on the facade.
   * @throws Exception the exception
   */
  public void shouldDestroyFailThenSelectedNotNull() throws Exception {
    FacesContext context = null;
    try {
      context = initializeFacesContext();
      when(context.isValidationFailed()).thenReturn(Boolean.TRUE);
      this.cut.destroy();
      verify(this.cut.ejbFacade, times(1)).remove(TEST_DISCOUNT_CODE);
      verify(context, times(1)).isValidationFailed();
      assertNotNull(this.cut.getSelected());
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
