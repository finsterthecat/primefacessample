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
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author tonybrouwer
 */
public class DiscountCodeControllerConverterTest {
  DiscountCodeController.DiscountCodeControllerConverter cut = new DiscountCodeController.DiscountCodeControllerConverter();
  DiscountCode TEST_DISCOUNT_CODE = new DiscountCode("A", new BigDecimal("2.2"));
  
  @Test
  /**
   * Destroy method for null selected should not call remove on the facade.
   * @throws Exception the exception
   */
  public void shouldGetKey() throws Exception {
    FacesContext context = null;

    try {
      context = initializeFacesContext();
      when(context.isValidationFailed()).thenReturn(Boolean.FALSE);
      Assert.assertEquals("Hello", this.cut.getKey("Hello"));
      Assert.assertEquals("Hello", this.cut.getStringKey("Hello"));
      Assert.assertEquals("Hello", this.cut.getKey("Hello"));
      Assert.assertEquals("A", this.cut.getAsString(context, null, TEST_DISCOUNT_CODE));
      Assert.assertNull("A", this.cut.getAsString(context, null, null));
      Assert.assertNull("A", this.cut.getAsString(context, null, "Not a discount code"));
      //Assert.assertEquals("A", this.cut.getAsObject(context, null, "A"));
    } finally {
      if (context != null) context.release();
    }
    
  }

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
