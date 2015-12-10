package com.brouwer.primefacessample;

import com.brouwer.primefacessample.model.DiscountCode;
import com.brouwer.primefacessample.model.util.JsfUtil;
import com.brouwer.primefacessample.model.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("discountCodeController")
@SessionScoped
public class DiscountCodeController implements Serializable {

  @EJB
  DiscountCodeFacade ejbFacade;
  private List<DiscountCode> items = null;
  private DiscountCode selected;

  public DiscountCodeController() {
  }

  public DiscountCode getSelected() {
    return selected;
  }

  public void setSelected(DiscountCode selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private DiscountCodeFacade getFacade() {
    return ejbFacade;
  }

  /**
   * Prepare a discount code for create.
   *
   * @return the discount code
   */
  public DiscountCode prepareCreate() {
    selected = new DiscountCode();
    initializeEmbeddableKey();
    return selected;
  }

  /**
   * Create a Discount Code in the database.
   */
  public void create() {
    persist(PersistAction.CREATE, ResourceBundle.getBundle("Bundle")
            .getString("DiscountCodeCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  /**
   * Update an existing Discount Code.
   */
  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("Bundle")
            .getString("DiscountCodeUpdated"));
  }

  /**
   * Destroy a discount code in the database.
   */
  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("Bundle")
            .getString("DiscountCodeDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  /**
   * Get all Discount Codes.
   *
   * @return list of discount codes
   */
  public List<DiscountCode> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    return items;
  }

  private void persist(PersistAction persistAction, String successMessage) {
    if (selected != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          getFacade().edit(selected);
        } else {
          getFacade().remove(selected);
        }
        JsfUtil.addSuccessMessage(successMessage);
      } catch (EJBException ex) {
        String msg = "";
        Throwable cause = ex.getCause();
        if (cause != null) {
          msg = cause.getLocalizedMessage();
        }
        if (msg.length() > 0) {
          JsfUtil.addErrorMessage(msg);
        } else {
          JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("Bundle")
                  .getString("PersistenceErrorOccured"));
        }
      }
    }
  }

  public DiscountCode getDiscountCode(java.lang.String id) {
    return getFacade().find(id);
  }

  public List<DiscountCode> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<DiscountCode> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = DiscountCode.class)
  public static class DiscountCodeControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext,
            UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      DiscountCodeController controller
              = (DiscountCodeController) facesContext.getApplication().getELResolver()
              .getValue(facesContext.getELContext(), null, "discountCodeController");
      return controller.getDiscountCode(getKey(value));
    }

    java.lang.String getKey(String value) {
      java.lang.String key;
      key = value;
      return key;
    }

    String getStringKey(java.lang.String value) {
      StringBuilder sb = new StringBuilder();
      sb.append(value);
      return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext,
            UIComponent component, Object object) {
      if (object == null) {
        return null;
      }
      if (object instanceof DiscountCode) {
        DiscountCode discountCode = (DiscountCode) object;
        return getStringKey(discountCode.getDiscountCode());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                "object {0} is of type {1}; expected type: {2}",
                new Object[]{object, object.getClass().getName(),
                  DiscountCode.class.getName()});
        return null;
      }
    }

  }

}
